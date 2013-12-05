package algo.graph.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import serialize.GraphSerialized;
import algo.graph.Graph;
import algo.graph.Node;
import algo.graph.SwingFXWebView;
import algo.graph.parsing.Parse;

public class MainPage 
{
	public JFrame f = new JFrame();
	public JPanel container = new JPanel();
	Image imageApp = Toolkit.getDefaultToolkit().getImage("src/ratp.png");
	public SwingFXWebView webView = new SwingFXWebView();
	
	public JLabel space = new JLabel();
	public JLabel space1 = new JLabel();
	public JLabel space2 = new JLabel();
	public JLabel space3 = new JLabel();
	public JLabel space4 = new JLabel();
	public JLabel space5 = new JLabel();
	public JLabel space6 = new JLabel();
	
	public JLabel modeTransport = new JLabel("Mode de transport                            ");	
	public JLabel critere = new JLabel("Critères                                                ");	
	public JLabel date = new JLabel("");	
	public SwingFXWebView traceWebView = new SwingFXWebView();
	public JTextField startTextField = new JTextField();
	public JTextField endTextField = new JTextField();
	public JLabel permutJLabel = new JLabel(new ImageIcon("src/LeftRight.png"));
	public JButton searchButton = new JButton("Rechercher");
	//public Parse parse = new Parse();
	public Graph g; // = parse.getGraph();
	
	// Mode de transport
	JRadioButton allButton   = new JRadioButton("TOUS"  , true);
	JRadioButton rerButton    = new JRadioButton("RER"   , false);
	JRadioButton metroButton = new JRadioButton("METRO", false);
	
	// Type travels
	JRadioButton fastTravels   = new JRadioButton("Le plus rapide"  , true);
	JRadioButton lessChangeTravels    = new JRadioButton("Le moins de correspondance     "   , false);
	JRadioButton astar    = new JRadioButton("A Star                                                "   , false);
	
	public JScrollPane pc = new JScrollPane();
	
	public GridBagLayout gbl = new GridBagLayout();
	public GridBagConstraints gbc = new GridBagConstraints();
	
	// Variable Img
	public static String imgTransport = "";
	public static String imgLigne = "";
	public static String imgHTMLTransport = "";
	
	public static String typeTransport = "";
	public static String critereTransport = "";
	
	public MainPage()
	{		
		f.setTitle("Algo Graph");
		f.setSize(550, 220);
		f.setIconImage(imageApp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setVisible(true);
		
		GraphSerialized deserialized  = null;
		try {
			deserialized = GraphSerialized.deSerialise("ressources/graph") ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"failed to load the graph");
		}
		g = deserialized.getSerializedGraph();
						
		// JTextField AutoComplete
		startTextField = AutoCompleteDocument.createAutoCompleteTextField(g.stops);
		endTextField = AutoCompleteDocument.createAutoCompleteTextField(g.stops);
			
		// SIZE
		startTextField.setPreferredSize(new Dimension(180, 20));
		endTextField.setPreferredSize(new Dimension(180, 20));
		permutJLabel.setPreferredSize(new Dimension(55,40));
		traceWebView.setPreferredSize(new Dimension(520,180));
		space.setPreferredSize(new Dimension(5,10));
		space1.setPreferredSize(new Dimension(5,10));
		space2.setPreferredSize(new Dimension(5,10));
		space3.setPreferredSize(new Dimension(5,10));
		space4.setPreferredSize(new Dimension(5,10));
		space5.setPreferredSize(new Dimension(5,10));
		space6.setPreferredSize(new Dimension(50,10));
		
		searchButton.setPreferredSize(new Dimension(105,20));
		searchButton.setBackground(Color.white);
		searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Create GridBagLayout
		f.setLayout(gbl);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(space, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(startTextField, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(permutJLabel, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(space1, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(endTextField, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(searchButton, gbc);
		
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(allButton);
		bgroup.add(rerButton);
		bgroup.add(metroButton);
		
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(1,3));
		radioPanel.add(allButton);
		radioPanel.add(rerButton);
		radioPanel.add(metroButton);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(radioPanel, gbc);
		
		ButtonGroup groupTypeTravel = new ButtonGroup();
		groupTypeTravel.add(fastTravels);
		groupTypeTravel.add(lessChangeTravels);
		groupTypeTravel.add(astar);
		
		JPanel radioPanelTravel = new JPanel();
		radioPanelTravel.setLayout(new BorderLayout());
		radioPanelTravel.add(fastTravels,BorderLayout.NORTH);
		radioPanelTravel.add(astar,BorderLayout.SOUTH);
		radioPanelTravel.add(lessChangeTravels,BorderLayout.LINE_END);
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(critere, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(radioPanelTravel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(modeTransport, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 11;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		f.add(date, gbc);
		
		// System.out.println(System.getProperty("java.class.path"));

		// LISTENER
		searchButton.addActionListener(eventSearchButton);
		
		permutJLabel.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) 
	        {
	        	String start = startTextField.getText();
	        	startTextField.setText(endTextField.getText());
	        	endTextField.setText(start);	
	        }
	    });
		
		// Permet de ne pas avoir  à  redimensionner la fen�tre
		f.validate();
		f.repaint();
	}
	
	ActionListener eventSearchButton = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			f.setSize(570, 850);
			f.setLocationRelativeTo(null);
			f.setResizable(false);
				
			// Check radioButton selected
			if(allButton.isSelected())
				typeTransport = "TOUS";
			
			if(rerButton.isSelected())
				typeTransport = "RER";
			
			if(metroButton.isSelected())
				typeTransport = "METRO";
			
			if(fastTravels.isSelected())
				critereTransport = "FAST";
			
			if(lessChangeTravels.isSelected())
				critereTransport = "CORRESPONDANCE";
			
			List<Node> chemin = null;
			if(astar.isSelected())
			{
				System.out.println("A STAR : ");
				chemin = g.AStar(g.node,startTextField.getText(),endTextField.getText(),typeTransport,critereTransport);
			}
				
			else
			{
				System.out.println("DIJKSTRA : ");
				chemin = g.Dijkstra(g.node,startTextField.getText(),endTextField.getText(),typeTransport,critereTransport);
			}
			
			if(chemin == null)
			{
				JOptionPane.showMessageDialog(f, "Erreur de recherche. Merci de vérifier les correspondances ou les stations que vous recherché.");
			}

			String ligne = "";
			
			String messageDisplay = "";
			String ligneTemp = "";
			ligneTemp = chemin.get(chemin.size()-1).line;
			
			String optionsInitMap = "var latlng = new google.maps.LatLng(" + g.node.get(chemin.get(chemin.size()-1).town).latitude + "," + g.node.get(chemin.get(chemin.size()-1).town).longitude + "); var options = {center: latlng,zoom: 13,mapTypeId: google.maps.MapTypeId.ROADMAP};";
			String initMap = "var carte = new google.maps.Map(document.getElementById('mapcanvas'), options);";
			String marqueur = "var imageMetro = new google.maps.MarkerImage('metro-ratp.png',new google.maps.Size(30,30),new google.maps.Point(0,0), new google.maps.Point(15,10));var imageRER = new google.maps.MarkerImage('rer-ratp.png',new google.maps.Size(30,30),new google.maps.Point(0,0), new google.maps.Point(15,10));var imageTram = new google.maps.MarkerImage('tram-ratp.png',new google.maps.Size(30,30),new google.maps.Point(0,0), new google.maps.Point(15,10));var imageStart = new google.maps.MarkerImage('icon_start.png',new google.maps.Size(30,30),new google.maps.Point(0,0), new google.maps.Point(5,30));var imageEnd = new google.maps.MarkerImage('icon_end.png',new google.maps.Size(30,30),new google.maps.Point(0,0), new google.maps.Point(5,30));";
			String displayParcourt = "<style>body { background-color : #EEEEEE;}td{padding:10px;}table {background-color : #EEEEEE;width:65%;}th{height:20px;}table, tr{border-right: 1px solid white;}table{font-size:13px;border-collapse:collapse;border-left : 1px solid white;border-bottom : 2px solid white;border-top : 1px solid white;border-right : 1px solid white;}</style><TABLE BORDER='1'>";
			
			String imgTransport = "";
			String imgHTMLTransport = "";
			String imgLigne = "";
			
			/* Traitement heure courante */
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
			String texte_date = sdf.format(new Date());
			int heureDepart = Integer.parseInt(texte_date.substring(0,2));
			int minuteDepart = Integer.parseInt(texte_date.substring(3,5));
			
			int lastHeureTemp = heureDepart;
			int lastminuteTemp = minuteDepart;
			int heureTemp = heureDepart;
			int minuteTemp = minuteDepart;
			
			refreshImg(g,chemin,chemin.size()-1);
						
			marqueur += "var marqueur = new google.maps.Marker({position: new google.maps.LatLng(" + g.node.get(chemin.get(chemin.size()-1).town).latitude + "," + g.node.get(chemin.get(chemin.size()-1).town).longitude + "),map: carte,title: '" + g.node.get(chemin.get(chemin.size()-1).town).town.replace("-"," ").replace("'","'\\") + "'," + MainPage.imgTransport + "}); attacherMessageSecret(marqueur, marqueur.title);";
			marqueur += "var marqueur = new google.maps.Marker({position: new google.maps.LatLng(" + g.node.get(chemin.get(chemin.size()-1).town).latitude + "," + g.node.get(chemin.get(chemin.size()-1).town).longitude + "),map: carte,title: '" + g.node.get(chemin.get(chemin.size()-1).town).town.replace("-"," ").replace("'","'\\") + "',icon:imageStart});";
			
			String parcourt = "var parcours = [ new google.maps.LatLng(" + g.node.get(chemin.get(chemin.size()-1).town).latitude + "," + g.node.get(chemin.get(chemin.size()-1).town).longitude + "),";
			
			messageDisplay += "De " + chemin.get(chemin.size()-1).town + " : " + chemin.get(chemin.size()-1).line;
			
			displayParcourt += "<TR> <TD>" + MainPage.imgHTMLTransport + MainPage.imgLigne + "</TD> <TD> depuis <strong>" + chemin.get(chemin.size()-1).town +"</strong><br/>"; 
			System.out.println("De " + chemin.get(chemin.size()-1).town + " : " + chemin.get(chemin.size()-1).line);
			
			int weightTotal = 0;
			int W = 0;
			int weight = 0;
			weight += getDistance(g,chemin.get(chemin.size()-1),chemin.get(chemin.size()-2));
			System.out.println("Distance entre " + chemin.get(chemin.size()-1) + " � " + chemin.get(chemin.size()-2) + " : " + weight);			
			
			for(int i = chemin.size()-1 ; i >= 0  ; i--)
			{
				if(!ligneTemp.equals(chemin.get(i).line))
				{
					System.out.println("Weight aux changements : " + weight);
					weightTotal += weight;
					displayParcourt += "jusqu'a <strong>" + chemin.get(i+1).town +"</strong></TD> <TD>" + lastHeureTemp + "h" + lastminuteTemp +  "<br>" + heureTemp + "h" + minuteTemp + "</TD> <TD>" + weight + "min </TD> </TR>";
					
					weight = 0;
					lastHeureTemp = heureTemp;
					lastminuteTemp = minuteTemp;
							
					System.out.println(" jusqua : " + chemin.get(i+1).town);
					
					refreshImg(g,chemin,i);
					
					marqueur += "var marqueur = new google.maps.Marker({position: new google.maps.LatLng(" + g.node.get(chemin.get(i).town).latitude + "," + g.node.get(chemin.get(i).town).longitude + "),map: carte,title: '" + g.node.get(chemin.get(i+1).town).town.replace("-"," ").replace("'","'\\")  + "'," + MainPage.imgTransport + "}); attacherMessageSecret(marqueur, marqueur.title);";
					parcourt += "new google.maps.LatLng(" + g.node.get(chemin.get(i).town).latitude + "," + g.node.get(chemin.get(i).town).longitude + "),";
					messageDisplay += "De " + chemin.get(i).town + " : " + chemin.get(i).line;
					
					displayParcourt += "<TR> <TD>"+ MainPage.imgHTMLTransport + MainPage.imgLigne + "</TD> <TD> depuis <strong>" + chemin.get(i+1).town +"</strong><br/>";
					System.out.println("De " + chemin.get(i+1).town + " : " + chemin.get(i).line);
				}
				
				ligneTemp = chemin.get(i).line;
				
				if(i != 0 && i != chemin.size()-1)
				{
					weight += getDistance(g,chemin.get(i),chemin.get(i-1));
					W = getDistance(g,chemin.get(i),chemin.get(i-1));
					
					minuteTemp += W;
					
					if(minuteTemp > 60)
					{
						minuteTemp -= 60;
						heureTemp += 1;
						
						if(minuteTemp < 10)
						{
							String min = "0" + minuteTemp;
							minuteTemp = Integer.parseInt(min);
						}
					}
				}
			}
			
			weightTotal += weight;
			System.out.println("Distance TOTAL : " + weightTotal);
			displayParcourt += "jusqu'a <strong>" + chemin.get(0).town +"</strong></TD> <TD>" + lastHeureTemp + "h" + lastminuteTemp +  "<br>" + heureTemp + "h" + minuteTemp + "</TD> <TD>" + weight + "min </TD> </TR>";
			//displayParcourt += "jusqu'a <strong>" + chemin.get(0).town +"</strong></TD> <TD> 14h41<br> 14h52 </TD> <TD> 11 min </TD> </TR> </TABLE>";
			messageDisplay += " jusqua : " + chemin.get(0).town;
			System.out.println(" jusqua : " + chemin.get(0).town);
			
			refreshImg(g,chemin,0);
			
			marqueur += "var marqueur = new google.maps.Marker({position: new google.maps.LatLng(" + g.node.get(chemin.get(0).town).latitude + "," + g.node.get(chemin.get(0).town).longitude + "),map: carte,title: '" + g.node.get(chemin.get(0).town).town.replace("-"," ").replace("'","'\\")  + "'," +MainPage.imgTransport + "}); attacherMessageSecret(marqueur, marqueur.title);";
			marqueur += "var marqueur = new google.maps.Marker({position: new google.maps.LatLng(" + g.node.get(chemin.get(0).town).latitude + "," + g.node.get(chemin.get(0).town).longitude + "),map: carte,title: '" + g.node.get(chemin.get(0).town).town.replace("-"," ").replace("'","'\\")  + "',icon:imageEnd});";
			
			parcourt += "new google.maps.LatLng(" + g.node.get(chemin.get(0).town).latitude + "," + g.node.get(chemin.get(0).town).longitude + ")];";
			parcourt += "var traceParcoursBus = new google.maps.Polyline({path: parcours,strokeColor: '#000000',strokeOpacity: 1.0,strokeWeight: 2}); traceParcoursBus.setMap(carte);";
			String function = "function attacherMessageSecret(marqueur, message) {var infoBulle = new google.maps.InfoWindow({content: message,size: new google.maps.Size(300,300)});google.maps.event.addListener(marqueur, 'click', function() {infoBulle.open(carte, marqueur);});} }";
			String cheminement = "<!DOCTYPE html><html><head><script src='http://maps.google.com/maps/api/js?sensor=false'></script><style>#mapcanvas { height: 380px; width: 505px}</style><script>var map;function loadmap() {" + optionsInitMap + initMap + marqueur + parcourt + function + "</script></head><body onload='loadmap()'><div id='mapcanvas'></div></body></html>";
			
			System.out.println("Cheminement : " + cheminement);

			gbc.gridx = 0;
			gbc.gridy = 11;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			f.add(space4, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 9;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			f.add(space3, gbc);
			
			f.remove(pc);
			f.validate();
			f.repaint();
			
			// Refresh Date
			int minuteFin = minuteDepart + weightTotal;
			int i =0;
			while(minuteFin > 60)
			{
				minuteFin -= 60;
				i++;
			}
			
			if(minuteFin < 10)
			{
				String min = "0" + minuteFin;
				minuteFin = Integer.parseInt(min);
			}
			
			int heureFin = heureDepart + i;
			
			//int minuteFin = Integer.parseInt(texte_date.substring(3,5));
            date.setText("Départ : " + heureDepart + "h" + minuteDepart + "                      Arrivée : " + heureFin + "h" + minuteFin);
			
			traceWebView = new SwingFXWebView(displayParcourt,"test1.html");
			traceWebView.setBackground(Color.decode("#EEEEEE"));
			traceWebView.setPreferredSize(new Dimension(500,450));
			
			pc = new JScrollPane(traceWebView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pc.setPreferredSize(new Dimension(530, 220));
			
			gbc.gridx = 0;
			gbc.gridy = 12;
			gbc.gridwidth = 5;
			gbc.gridheight = 4;
			f.add(pc, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 17;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			f.add(space5, gbc);
				
			f.remove(webView);
			f.validate();
			f.repaint();
						
			webView = new SwingFXWebView(cheminement,"test.html");
			webView.setPreferredSize(new Dimension(520,395));
			webView.setBackground(Color.decode("#EEEEEE"));
			
			gbc.gridx = 0;
			gbc.gridy = 18;
			gbc.gridwidth = 5;
			gbc.gridheight = 4;
			f.add(webView, gbc);
			
			// Permet de ne pas avoir � redimensionner la fen�tre
			f.validate();
			f.repaint();
			
			// g.Init(g.node,g.node.get(startTextField.getText()));
		}
	};
	
	
	public static void refreshImg(Graph g,List<Node> chemin,int i)
	{
		if(g.node.get(chemin.get(i).town).line.contains("T"))
		{
			imgTransport = "icon:imageTram";
			
			imgHTMLTransport = "<img src='logo_ratp/tramway.png'>";
			switch(g.node.get(chemin.get(i).town).line)
			{
				case "T1":
					imgLigne = "<img src='logo_ratp/tramway_1.png'>";
					break;
					
				case "T2":
					imgLigne = "<img src='logo_ratp/tramway_2.png'>";
					break;	
			}
		}
		
		if(g.node.get(chemin.get(i).town).line.equals("A") || g.node.get(chemin.get(i).town).line.equals("B"))
		{
			imgTransport = "icon:imageRER";
			
			imgHTMLTransport = "<img src='logo_ratp/rer.png'>";
			switch(g.node.get(chemin.get(i).town).line)
			{
				case "A":
					imgLigne = "<img src='logo_ratp/rera.png'>";
					break;
					
				case "B":
					imgLigne = "<img src='logo_ratp/rerb.png'>";
					break;
			}
		}
		
		else
		{
			System.out.println("METRO : " + g.node.get(chemin.get(i).town).line) ;
			imgTransport = "icon:imageMetro";
			
			imgHTMLTransport = "<img src='logo_ratp/metro.png'>";
			switch(g.node.get(chemin.get(i).town).line)
			{
				case "1":
					imgLigne = "<img src='logo_ratp/ligne_metro1.png'>";
					break;
					
				case "2":
					imgLigne = "<img src='logo_ratp/ligne_metro2.png'>";
					break;	
					
				case "3":
					imgLigne = "<img src='logo_ratp/ligne_metro3.png'>";
					break;	
					
				case "3B":
					imgLigne = "<img src='logo_ratp/ligne_metro3bis.png'>";
					break;	
					
				case "4":
					imgLigne = "<img src='logo_ratp/ligne_metro4.png'>";
					break;	
					
				case "5":
					imgLigne = "<img src='logo_ratp/ligne_metro5.png'>";
					break;	
					
				case "6":
					imgLigne = "<img src='logo_ratp/ligne_metro6.png'>";
					break;	
					
				case "7":
					imgLigne = "<img src='logo_ratp/ligne_metro7.png'>";
					break;	
					
				case "7B":
					imgLigne = "<img src='logo_ratp/ligne_metro7bis.png'>";
					break;	
					
				case "8":
					imgLigne = "<img src='logo_ratp/ligne_metro8.png'>";
					break;	
					
				case "9":
					imgLigne = "<img src='logo_ratp/ligne_metro9.png'>";
					break;	
					
				case "10":
					imgLigne = "<img src='logo_ratp/ligne_metro10.png'>";
					break;	
					
				case "11":
					imgLigne = "<img src='logo_ratp/ligne_metro11.png'>";
					break;	
					
				case "12":
					imgLigne = "<img src='logo_ratp/ligne_metro12.png'>";
					break;	
					
				case "13":
					imgLigne = "<img src='logo_ratp/ligne_metro13.png'>";
					break;	
					
				case "14":
					imgLigne = "<img src='logo_ratp/ligne_metro14.png'>";
					break;	
			}
		}
	}
	
	public static int getDistance(Graph g,Node villeStart,Node villeEnd)
	{
		for(int i = 0 ;  i < g.node.get(villeStart.town).getRelations().size() ; i++)
		{
			if(g.node.get(villeStart.town).getRelations().get(i).getEndNode().town.equals(villeEnd.town))
			{
				return g.node.get(villeStart.town).getRelations().get(i).getWeight();
			}
		}
		
		return 0;
	}
	
	public static void main(String[] args)
	{
		new MainPage();
	}
}
