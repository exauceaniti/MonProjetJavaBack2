package panels;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ContactHeaderPanel extends JPanel {
    private final JTextField searchField;
    private final JLabel titleLabel;
    private SearchListener listener;

    public interface SearchListener {
        void onSearch(String query);
        void onReset();
    }

    public ContactHeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(80, 85, 100));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 🔹 Titre à gauche
        titleLabel = new JLabel("Liste des contacts");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.WEST);

        // 🔍 Barre de recherche à droite
        searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Placeholder
                    g2.setColor(new Color(130, 130, 130));
                    g2.setFont(getFont().deriveFont(Font.PLAIN));
                    g2.drawString("Rechercher", 40, getHeight() / 2 + 5);

                    // Loupe
                    g2.setStroke(new BasicStroke(1.3f));
                    g2.drawOval(12, getHeight() / 2 - 8, 16, 16);
                    g2.drawLine(20, getHeight() / 2 + 4, 26, getHeight() / 2 + 10);
                    g2.dispose();
                }
            }
        };

        configureSearchField();

        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setOpaque(false);
        rightContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        rightContainer.add(searchField, BorderLayout.CENTER);
        add(rightContainer, BorderLayout.EAST);
    }

    private void configureSearchField() {
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setForeground(Color.BLACK);
        searchField.setCaretColor(Color.BLACK);
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setPreferredSize(new Dimension(280, 40));
        searchField.setBorder(new RoundedBorder(16));

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { searchField.repaint(); }
            public void focusLost(FocusEvent e) { searchField.repaint(); }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handle(); }
            public void removeUpdate(DocumentEvent e) { handle(); }
            public void changedUpdate(DocumentEvent e) { handle(); }

            private void handle() {
                String text = searchField.getText().trim();
                if (listener == null) return;
                if (text.isEmpty()) listener.onReset();
                else listener.onSearch(text);
            }
        });
    }

    public void setSearchListener(SearchListener listener) {
        this.listener = listener;
    }

    public void resetSearch() {
        searchField.setText("");
        searchField.repaint();
        if (listener != null) listener.onReset();
    }

    public JTextField getSearchField() {
        return searchField;
    }

    // 🧼 Bordure douce et arrondie
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(8, 40, 8, 20);
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(8, 40, 8, 20);
            return insets;
        }
    }
}
