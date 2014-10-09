import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.awt.Desktop;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Launcher extends JFrame {

	JFrame downloadWindow;
	
	private String versionURL = "https://dl.dropboxusercontent.com/u/31306161/vscape/clientVersion.dat";
	private String mirrorUrl = "https://dl.dropboxusercontent.com/u/31306161/vscape/downloadUrl.dat";
	private int version = 1;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
			JFrame.setDefaultLookAndFeelDecorated(true);
			Launcher launcher = new Launcher(args);
			launcher.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Launcher(final String[] args) throws IOException{
		super("/v/scape Client Launcher");
		
		this.setSize(new Dimension(300, 229));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		getContentPane().setLayout(null);
		
		boolean upToDate = upToDate();
		
		String updateVer = "Up To Date";
		if(!upToDate){
			updateVer = "Update Available";
		}
		
		JLabel label = new JLabel(updateVer, JLabel.CENTER);
		label.setFont(new Font("Arial", Font.PLAIN, 24));
		label.setBounds(50, 50, 200, 30);
		getContentPane().add(label);
		
		if(!upToDate){
			JButton updateBtn = new JButton("Download");
			updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try {
						openUpWebSite(downloadUrl());
					} catch (IOException e) {
					}
				}
			});
			updateBtn.setBounds(75, 90, 150, 38);
			getContentPane().add(updateBtn);
		}
		
		
		JButton Launch = new JButton("Launch");
		Launch.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				try {
					setVisible(false);
					dispose();
					Client client = new Client();
					client.main(args);
				} catch (Exception e) {
				}
			}
		});
		if(!upToDate){
			Launch.setBounds(100, 152, 100, 38);
		}else{
			Launch.setBounds(100, 90, 100, 38);
		}
		getContentPane().add(Launch);
	}
	
	private boolean upToDate() throws IOException {
		try {
			BufferedReader cacheVerReader = new BufferedReader(new InputStreamReader(new URL(versionURL).openStream()));
			String line;
			try {
				while((line = cacheVerReader.readLine()) != null) {
					return Integer.parseInt(line) == version;
				}
			} catch(IOException e) {
				System.out.println("problem reading remote version");
				return false;
			} finally {
				if(cacheVerReader != null) {
					try {
						cacheVerReader.close();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		} catch(IOException e) {
			System.out.println("Could not fetch version");
			return false;
		}
		return false;
    }
	
	private String downloadUrl() throws IOException {
		try {
			BufferedReader cacheVerReader = new BufferedReader(new InputStreamReader(new URL(mirrorUrl).openStream()));
			String line;
			try {
				while((line = cacheVerReader.readLine()) != null) {
					return line;
				}
			} catch(IOException e) {
				System.out.println("problem reading download url");
				return null;
			} finally {
				if(cacheVerReader != null) {
					try {
						cacheVerReader.close();
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		} catch(IOException e) {
			System.out.println("Could not fetch download url");
			return null;
		}
		return null;
    }
	
	private static void openUpWebSite(String url) {
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI(url)); 	
		} catch (Exception e) {
		}
	}
}
