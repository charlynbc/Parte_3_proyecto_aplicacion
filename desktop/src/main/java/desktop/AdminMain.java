package desktop;

import uy.edu.pa.central.client.ActividadesService;
import uy.edu.pa.central.client.ActividadesService_Service;
import uy.edu.pa.central.client.AuthService;
import uy.edu.pa.central.client.AuthService_Service;
import uy.edu.pa.central.client.UserDTO;
import uy.edu.pa.central.client.ActividadDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminMain extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JDesktopPane desktopPane;
    private JMenuBar menuBar;
    private boolean loggedIn = false;

    public AdminMain() {
        setTitle("Turismo.uy - Estación de Trabajo (SOAP)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de login inicial
        JPanel loginPanel = new JPanel(new GridLayout(4, 2));
        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginPanel.add(loginButton);

        statusLabel = new JLabel("");
        loginPanel.add(statusLabel);

        add(loginPanel, BorderLayout.NORTH);

        // Desktop pane para internal frames
        desktopPane = new JDesktopPane();
        add(desktopPane, BorderLayout.CENTER);

        // Menu bar (habilitado después de login)
        menuBar = new JMenuBar();
        JMenu usuariosMenu = new JMenu("Usuarios");
        JMenuItem consultarUsuariosItem = new JMenuItem("Consultar Usuarios");
        consultarUsuariosItem.addActionListener(e -> openUsuariosFrame());
        usuariosMenu.add(consultarUsuariosItem);
        menuBar.add(usuariosMenu);

        JMenu actividadesMenu = new JMenu("Actividades");
        JMenuItem consultarActividadesItem = new JMenuItem("Consultar Actividades");
        consultarActividadesItem.addActionListener(e -> openActividadesFrame());
        actividadesMenu.add(consultarActividadesItem);
        menuBar.add(actividadesMenu);

        JMenu salidasMenu = new JMenu("Salidas");
        JMenuItem consultarSalidasItem = new JMenuItem("Consultar Salidas");
        consultarSalidasItem.addActionListener(e -> openSalidasFrame());
        salidasMenu.add(consultarSalidasItem);
        menuBar.add(salidasMenu);

        JMenu inscripcionesMenu = new JMenu("Inscripciones");
        JMenuItem consultarInscripcionesItem = new JMenuItem("Consultar Inscripciones");
        consultarInscripcionesItem.addActionListener(e -> openInscripcionesFrame());
        inscripcionesMenu.add(consultarInscripcionesItem);
        menuBar.add(inscripcionesMenu);

        setJMenuBar(menuBar);
        menuBar.setVisible(false); // Oculto hasta login

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            AuthService_Service svc = new AuthService_Service();
            AuthService port = svc.getAuthServicePort();
            UserDTO user = port.login(username, password);

            if (user != null && user.getNickname() != null) {
                statusLabel.setText("Login exitoso: " + user.getNombre());
                loggedIn = true;
                menuBar.setVisible(true);
                // Ocultar panel de login
                getContentPane().remove(1); // Remover login panel
                revalidate();
                repaint();
            } else {
                statusLabel.setText("Credenciales inválidas");
            }
        } catch (Exception ex) {
            statusLabel.setText("Error SOAP: " + ex.getMessage());
        }
    }

    private void openUsuariosFrame() {
        if (!loggedIn) return;
        JInternalFrame frame = new JInternalFrame("Consultar Usuarios", true, true, true, true);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Simular consulta (en realidad, si hay servicio, llamarlo)
        String[] columnNames = {"Nickname", "Nombre", "Email", "Tipo"};
        Object[][] data = {
            {"admin", "Admin", "admin@turismo.uy", "Usuario"},
            // Aquí llamar SOAP si existe método listarUsuarios
        };
        JTable table = new JTable(data, columnNames);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void openSalidasFrame() {
        if (!loggedIn) return;
        JInternalFrame frame = new JInternalFrame("Consultar Salidas", true, true, true, true);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Simular consulta (en realidad, llamar SOAP si existe)
        String[] columnNames = {"Nombre", "Actividad", "Fecha", "Máx Turistas"};
        Object[][] data = {
            {"Salida A", "Actividad 1", "2025-12-01", "10"},
            {"Salida B", "Actividad 2", "2025-12-05", "15"},
        };
        JTable table = new JTable(data, columnNames);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void openInscripcionesFrame() {
        if (!loggedIn) return;
        JInternalFrame frame = new JInternalFrame("Consultar Inscripciones", true, true, true, true);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Simular consulta (en realidad, llamar SOAP si existe)
        String[] columnNames = {"Turista", "Salida", "Fecha Inscripción", "Costo"};
        Object[][] data = {
            {"turista1", "Salida A", "2025-11-12", "$1000"},
            {"turista2", "Salida B", "2025-11-10", "$1500"},
        };
        JTable table = new JTable(data, columnNames);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        desktopPane.add(frame);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminMain());
    }
}