package com.kweezy.singeditor.ui;

import com.kweezy.singeditor.config.SingBoxConfig;
import com.kweezy.singeditor.config.LogConfig;
import com.kweezy.singeditor.config.DnsConfig;
import com.kweezy.singeditor.config.NtpConfig;
import com.kweezy.singeditor.config.CertificateConfig;
import com.kweezy.singeditor.config.RouteConfig;
import com.kweezy.singeditor.config.Service;
import com.kweezy.singeditor.config.experimental.ExperimentalConfig;
import com.kweezy.singeditor.config.inbound.TypedInbound;
import com.kweezy.singeditor.config.outbound.TypedOutbound;
import com.kweezy.singeditor.config.endpoint.TypedEndpoint;
import com.kweezy.singeditor.ui.util.ScrollUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SingBoxConfigEditorPanel extends JPanel {

    private final JTabbedPane tabbedPane;

    // Editors for object sections
    private final GenericObjectEditorPanel<LogConfig> logEditor;
    private final GenericObjectEditorPanel<DnsConfig> dnsEditor;
    private final GenericObjectEditorPanel<NtpConfig> ntpEditor;
    private final GenericObjectEditorPanel<CertificateConfig> certEditor;
    private final GenericObjectEditorPanel<RouteConfig> routeEditor;
    private final GenericObjectEditorPanel<ExperimentalConfig> expEditor;

    // Editors for list sections
    private final ListEditorPanel<TypedInbound> inboundsEditor;
    private final ListEditorPanel<TypedOutbound> outboundsEditor;
    private final ListEditorPanel<TypedEndpoint> endpointsEditor;
    private final ListEditorPanel<Service> servicesEditor;

    public SingBoxConfigEditorPanel() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Initialize editors
        logEditor = new GenericObjectEditorPanel<>(LogConfig.class);
        dnsEditor = new GenericObjectEditorPanel<>(DnsConfig.class);
        ntpEditor = new GenericObjectEditorPanel<>(NtpConfig.class);
        certEditor = new GenericObjectEditorPanel<>(CertificateConfig.class);
        routeEditor = new GenericObjectEditorPanel<>(RouteConfig.class);
        expEditor = new GenericObjectEditorPanel<>(ExperimentalConfig.class);

        inboundsEditor = new ListEditorPanel<>(TypedInbound.class);
        outboundsEditor = new ListEditorPanel<>(TypedOutbound.class);
        endpointsEditor = new ListEditorPanel<>(TypedEndpoint.class);
        servicesEditor = new ListEditorPanel<>(Service.class);

        // Add tabs
        addTab("Log", logEditor);
        addTab("DNS", dnsEditor);
        addTab("Inbounds", inboundsEditor);
        addTab("Outbounds", outboundsEditor);
        addTab("Route", routeEditor);
        addTab("Endpoints", endpointsEditor);
        addTab("Services", servicesEditor);
        addTab("NTP", ntpEditor);
        addTab("Certificate", certEditor);
        addTab("Experimental", expEditor);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void addTab(String title, JComponent component) {
        if (component instanceof GenericObjectEditorPanel) {
            JScrollPane sp = new JScrollPane(component);
            ScrollUtil.configureScrollPane(sp);
            tabbedPane.addTab(title, sp);
        } else {
            tabbedPane.addTab(title, component);
        }
    }

    public void setConfig(SingBoxConfig config) {
        if (config == null) config = new SingBoxConfig();

        logEditor.setObject(config.getLog() != null ? config.getLog() : new LogConfig());
        dnsEditor.setObject(config.getDns() != null ? config.getDns() : new DnsConfig());
        ntpEditor.setObject(config.getNtp() != null ? config.getNtp() : new NtpConfig());
        certEditor.setObject(config.getCertificate() != null ? config.getCertificate() : new CertificateConfig());
        routeEditor.setObject(config.getRoute() != null ? config.getRoute() : new RouteConfig());
        expEditor.setObject(config.getExperimental() != null ? config.getExperimental() : new ExperimentalConfig());

        inboundsEditor.setList(config.getInbounds() != null ? config.getInbounds() : new ArrayList<>());
        outboundsEditor.setList(config.getOutbounds() != null ? config.getOutbounds() : new ArrayList<>());
        endpointsEditor.setList(config.getEndpoints() != null ? config.getEndpoints() : new ArrayList<>());
        servicesEditor.setList(config.getServices() != null ? config.getServices() : new ArrayList<>());
    }

    public SingBoxConfig getConfig() {
        SingBoxConfig config = new SingBoxConfig();
        
        config.setLog(logEditor.getObject());
        config.setDns(dnsEditor.getObject());
        config.setNtp(ntpEditor.getObject());
        config.setCertificate(certEditor.getObject());
        config.setRoute(routeEditor.getObject());
        config.setExperimental(expEditor.getObject());

        config.setInbounds(inboundsEditor.getList());
        config.setOutbounds(outboundsEditor.getList());
        config.setEndpoints(endpointsEditor.getList());
        config.setServices(servicesEditor.getList());

        return config;
    }
}
