/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aubot.agv;

import com.aubot.agv.attributes.AgvAttribute;
import com.aubot.agv.attributes.Attribute;
import com.aubot.agv.attributes.AttributeFactory;
import com.aubot.agv.ulti.*;
import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 *
 * @author ADMIN
 */
public class ConfigurationFrame extends javax.swing.JFrame {

    private final Map<String, ConfigurationPanel> configurationPanels = new LinkedHashMap<>();

    private File currentFile;

    private AgvDevice agvDevice;

    SerialPort port;
    JButton btnRfidConfig = new JButton("Rfid Config");

    /** Creates new form ConfigurationFrame */
    public ConfigurationFrame() {
        initComponents();
        initAttributes();
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.initEvents();
        this.setTitle("AGV CONFIG - AUBOT");
        this.lblStatus.setText("");
        this.lblFileName.setText("");
        this.btnConfigure.setEnabled(false);
    }

    private void initEvents() {
        btnLoadConfigs.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON", "json"));
            int selected = fileChooser.showOpenDialog(this);
            if (selected == JFileChooser.APPROVE_OPTION) {
                try {
                    JsonObject configs = new Gson().fromJson(new FileReader(fileChooser.getSelectedFile()), JsonObject.class);
                    if (configs == null) {
                        return;
                    }
                    configurationPanels.values().forEach(ConfigurationPanel::clear);
                    configs.keySet().forEach(key -> {
                        ConfigurationPanel panel = configurationPanels.get(key);
                        if (panel == null) {
                            return;
                        }
                        if (panel instanceof NumberConfigPanel) {
                            panel.setAttributeValue(configs.get(key).getAsInt());
                        } else if (panel instanceof SelectionConfigPanel) {
                            panel.setAttributeValue(configs.get(key).getAsString());
                        } else if (panel instanceof CheckConfigPanel) {
                            panel.setAttributeValue(configs.get(key).getAsInt());
                        }
                    });
                    currentFile = fileChooser.getSelectedFile();
                    lblFileName.setText(currentFile.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSaveConfigs.addActionListener(e -> {
            if (currentFile != null) {
                if (currentFile.exists()) {
                    int status = JOptionPane.showConfirmDialog(this, "Override current file?",
                            "Save config", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (status == JOptionPane.YES_OPTION) {
                        saveToFile(currentFile);
                    } else if (status == JOptionPane.NO_OPTION) {
                        saveToFile(null);
                    }
                    return;
                }
            }
            saveToFile(null);
        });

        cbxPort.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                cbxPort.removeAllItems();
                SerialPort[] ports = SerialPort.getCommPorts();
                for (SerialPort serialPort : ports) {
                    cbxPort.addItem(serialPort.getSystemPortName());
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

        tgbConnect.addActionListener(e -> {
            if (tgbConnect.isSelected()) {
                if (cbxPort.getSelectedIndex() < 0) {
                    tgbConnect.setSelected(false);
                    return;
                }
                port = SerialPort.getCommPort((String) cbxPort.getSelectedItem());
                new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        HalfDuplexCommunication hdc = new SerialCommunication(port);
                        agvDevice = new AgvDevice(hdc, new MixHandlerBuilder());
                        try {
                            for (ConfigurationPanel panel : configurationPanels.values()) {
                                Attribute attr = panel.getAttribute();
                                agvDevice.getAttribute(attr);
                                panel.setAttributeValue(attr.getValue());
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void done() {
                        super.done();
                        try {
                            get();
                            lblStatus.setText("Ready");
                            lblStatus.setForeground(Color.green.darker());
                        } catch (InterruptedException | ExecutionException ex) {
                            tgbConnect.setSelected(false);
                            handleError(ex);
                        }
                    }
                }.execute();
            } else {
                if (port.isOpen()) {
                    port.closePort();
                }
                agvDevice = null;
                lblStatus.setText("");
            }
        });

        tgbConnect.addItemListener(e -> {
            boolean checked = e.getStateChange() == ItemEvent.SELECTED;
            tgbConnect.setText(checked ? "Disconnect" : "Connect");
            btnConfigure.setEnabled(checked);
        });

        btnConfigure.addActionListener(e -> {
            if (agvDevice == null) {
                return;
            }
            new SwingWorker<String, Integer>() {
                @Override
                protected String doInBackground() throws Exception {
                    java.util.List<Attribute> attributes = configurationPanels.values().stream()
                            .map(ConfigurationPanel::getAttribute)
                            .filter(attr -> attr.getValue() != null)
                            .collect(Collectors.toList());
                    for (int i = 0; i < attributes.size(); i++) {
                        agvDevice.setAttribute(attributes.get(i));
                        process(Arrays.asList(i + 1, attributes.size()));
                    }

                    return "Configured!";
                }

                @Override
                protected void process(List<Integer> chunks) {
                    super.process(chunks);
                    processBar.setValue(chunks.get(0) / chunks.get(1) * 100);
                }

                @Override
                protected void done() {
                    try {
                        JOptionPane.showMessageDialog(ConfigurationFrame.this, get());
                    } catch (InterruptedException | ExecutionException ex) {
                        handleError(ex);
                    }
                }
            }.execute();
        });

        btnRfidConfig.addActionListener(e -> {
            new RfidConfigPanel().setVisible(true);
        });
    }

    private void handleError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void saveToFile(File file) {
        if (file == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON", "json"));
            fileChooser.setCurrentDirectory(currentFile);
            int selected = fileChooser.showSaveDialog(this);
            if (selected != JFileChooser.APPROVE_OPTION) {
                return;
            }
            file = fileChooser.getSelectedFile();
        }
        JsonObject configs = new JsonObject();
        configurationPanels.forEach((attribute, panel) -> {
            Object value = panel.getAttribute().getValue();
            if (value == null) {
                return;
            }
            configs.addProperty(attribute, panel.getAttribute().getValue().toString());
        });
        if (!file.getName().endsWith(".json")) {
            file = new File(file.getAbsolutePath().concat(".json"));
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            GSON.toJson(configs, fileWriter);
            setCurrentFile(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void setCurrentFile(File file) {
        currentFile = file;
        lblFileName.setText(currentFile.getName());
    }

    private void initAttributes() {
        configurationPanels.put(AgvAttribute.OB_DISTANCE_TRUOC,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.OB_DISTANCE_TRUOC), 0, 80));
        configurationPanels.put(AgvAttribute.OB_DISTANCE_CHEO,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.OB_DISTANCE_CHEO), 0, 80));
        configurationPanels.put(AgvAttribute.OB_DISTANCE_CANH,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.OB_DISTANCE_CANH), 0, 80));

        configurationPanels.put(AgvAttribute.TACT_TIME,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.TACT_TIME), 0, 1000));
        configurationPanels.put(AgvAttribute.TABLE_LENGTH,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.TABLE_LENGTH), 0, 1000));

        configurationPanels.put(AgvAttribute.IN_CURVE,
                new CheckConfigPanel(AttributeFactory.createAttribute(AgvAttribute.IN_CURVE)));
        configurationPanels.put(AgvAttribute.SYNC_ENABLE,
                new CheckConfigPanel(AttributeFactory.createAttribute(AgvAttribute.SYNC_ENABLE)));

        configurationPanels.put(AgvAttribute.LOW_BATTERY_LEVEL,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.LOW_BATTERY_LEVEL), 0, 99));
        configurationPanels.put(AgvAttribute.OUT_BATTERY_LEVEL,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.OUT_BATTERY_LEVEL), 0, 99));

        configurationPanels.put(AgvAttribute.START_VOLUME,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.START_VOLUME), 0, 80));
        configurationPanels.put(AgvAttribute.ALARM_VOLUME,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.ALARM_VOLUME), 0, 80));
        configurationPanels.put(AgvAttribute.WARNING_VOLUME,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.WARNING_VOLUME), 0, 80));
        configurationPanels.put(AgvAttribute.SWAP_VOLUME,
                new NumberConfigPanel(AttributeFactory.createAttribute(AgvAttribute.SWAP_VOLUME), 0, 80));

        configurationPanels.values().forEach(pnlConfigs::add);

        rfidConfigPanel =  new JPanel();
        rfidConfigPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        JLabel lblConfig = new JLabel("Config Rfid: ");
        g.weightx = 1;
        rfidConfigPanel.add(lblConfig,g);
        g.gridx = 1;
        g.weightx = 3;
        rfidConfigPanel.add(btnRfidConfig,g);

        pnlConfigs.add(rfidConfigPanel);


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        pnlConnection = new javax.swing.JPanel();
        lblPort = new javax.swing.JLabel();
        cbxPort = new javax.swing.JComboBox<>();
        tgbConnect = new javax.swing.JToggleButton();
        lblStatus = new javax.swing.JLabel();
        pnlConfigfile = new javax.swing.JPanel();
        lblFileName = new javax.swing.JLabel();
        btnLoadConfigs = new javax.swing.JButton();
        btnSaveConfigs = new javax.swing.JButton();
        pnlProcess = new javax.swing.JPanel();
        processBar = new javax.swing.JProgressBar();
        btnConfigure = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        pnlConfigs = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(669, 50));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING);
        flowLayout1.setAlignOnBaseline(true);
        pnlConnection.setLayout(flowLayout1);

        lblPort.setText("Port:");
        pnlConnection.add(lblPort);

        cbxPort.setPreferredSize(new java.awt.Dimension(200, 29));
        pnlConnection.add(cbxPort);

        tgbConnect.setText("Connect");
        pnlConnection.add(tgbConnect);

        lblStatus.setText("status...");
        pnlConnection.add(lblStatus);

        jPanel1.add(pnlConnection);

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING);
        flowLayout2.setAlignOnBaseline(true);
        pnlConfigfile.setLayout(flowLayout2);

        lblFileName.setText("File...");
        pnlConfigfile.add(lblFileName);

        btnLoadConfigs.setText("Load");
        pnlConfigfile.add(btnLoadConfigs);

        btnSaveConfigs.setText("Save");
        pnlConfigfile.add(btnSaveConfigs);


        jPanel1.add(pnlConfigfile);


        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pnlProcess.setPreferredSize(new java.awt.Dimension(669, 40));
        pnlProcess.setLayout(new java.awt.GridBagLayout());

        processBar.setPreferredSize(new java.awt.Dimension(183, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        pnlProcess.add(processBar, gridBagConstraints);

        btnConfigure.setText("Configure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        pnlProcess.add(btnConfigure, gridBagConstraints);

        getContentPane().add(pnlProcess, java.awt.BorderLayout.PAGE_END);

        pnlConfigs.setLayout(new java.awt.GridLayout(0, 2, 10, 10));
        scrollPane.setViewportView(pnlConfigs);
        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        FlatLightLaf.setup();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConfigurationFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfigure;
    private javax.swing.JButton btnLoadConfigs;
    private javax.swing.JButton btnSaveConfigs;
    private javax.swing.JComboBox<String> cbxPort;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel pnlConfigfile;
    private javax.swing.JPanel pnlConfigs;
    private javax.swing.JPanel pnlConnection;
    private javax.swing.JPanel pnlProcess;
    private javax.swing.JProgressBar processBar;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JToggleButton tgbConnect;
    private JPanel rfidConfigPanel;
    // End of variables declaration//GEN-END:variables

}
