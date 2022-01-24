/*
	Program: Prosta aplikacja wielowątkowa
	Plik: MainFrame.java
	autor: Paul Paczyński
	data: styczeń 2022



	KLasa MainFrame obsługuje GUI programu i posiada w sobie metodę main()
	GUI zbudowane za pomocą WindowBuildera w programie Eclipse
	Program zaczyna działać automatycznie podczas startu.

*/



package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import data.Bus;
import data.NarrowBridge;

public class MainFrame extends JFrame  implements WindowListener {

	private DefaultCaret caret;
	private JPanel contentPane;
	private JTextField textQueue;
	private JTextField textBridge;
	private JTextArea textLog;
	protected NarrowBridge bridge = new NarrowBridge(MainFrame.this);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Narrow Bridge");
		;
		this.addWindowListener(this);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 531, 652);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.background"));
		panel.setBounds(0, 218, 512, 395);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		textLog = new JTextArea();
		caret = (DefaultCaret) textLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textLog.setEditable(false);
		scrollPane.setViewportView(textLog);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(UIManager.getColor("Button.background"));
		panel_1.setBounds(10, 0, 502, 211);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lbKolejka = new JLabel("Kolejka");
		lbKolejka.setBounds(395, 9, 62, 26);
		lbKolejka.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lbKolejka);

		textQueue = new JTextField();
		textQueue.setEditable(false);
		textQueue.setBounds(10, 4, 375, 37);
		panel_1.add(textQueue);
		textQueue.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Tryb Przejazdu");
		lblNewLabel_2.setBounds(166, 163, 91, 20);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblNewLabel_2);

		textBridge = new JTextField();
		textBridge.setEditable(false);
		textBridge.setBounds(10, 52, 375, 37);
		panel_1.add(textBridge);
		textBridge.setColumns(10);

		JLabel lbMost = new JLabel("Na moscie ");
		lbMost.setBounds(395, 57, 82, 26);
		lbMost.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lbMost);

		JSlider sliderTraffic = new JSlider();
		sliderTraffic.setBounds(10, 100, 375, 49);
		sliderTraffic.setPaintTicks(true);
		sliderTraffic.setMajorTickSpacing(4999);
		sliderTraffic.setMaximum(5000);
		sliderTraffic.setValue(0);
		Hashtable<Integer, JLabel> labels = new Hashtable<>();
		labels.put(0, new JLabel("Maly"));
		labels.put(5000, new JLabel("Duzy"));
		sliderTraffic.setLabelTable(labels);
		sliderTraffic.setPaintLabels(true);
		;
		sliderTraffic.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				bridge.traffic = sliderTraffic.getValue();

			}
		});
		panel_1.add(sliderTraffic);

		JLabel lbTraffic = new JLabel("Natezenie ruchu");
		lbTraffic.setBounds(395, 94, 122, 31);
		lbTraffic.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lbTraffic);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(
				new String[] { "Bez ograniczen", "Tylko jeden bus" }));
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bridge.setTrafficType(comboBox.getSelectedIndex());
			}
		});
		comboBox.setBounds(10, 160, 146, 26);
		panel_1.add(comboBox);

	}

	private void start() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				while (true) {
					Bus bus = new Bus(bridge);
					new Thread(bus).start();

					try {
						Thread.sleep(5500 - bridge.traffic);
					} catch (InterruptedException e) {
					}
				}
			}

		};
		worker.execute();
	}

	public void updateQueue(String list) {
		textQueue.setText(list);

	}

	public void updateBridge(String list) {
		textBridge.setText(list);

	}

	public void updateLog(String list) {
		textLog.append(list + "\n");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		start();

		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}




