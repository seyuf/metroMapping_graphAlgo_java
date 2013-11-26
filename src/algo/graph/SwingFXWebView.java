package algo.graph;

import com.sun.javafx.application.PlatformImpl;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;

import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.JButton;
import javax.swing.JPanel;
  
public class SwingFXWebView extends JPanel 
{    
    private Stage stage;  
    private WebView browser;  
    private JFXPanel jfxPanel;  
    private JButton swingButton;  
    private WebEngine webEngine;  
    private String contentHTML;
    private String nameFile;
  
    public SwingFXWebView(String content,String type)
    {  
        initComponents();
        contentHTML = content;
        nameFile = type;
    }
    
    public SwingFXWebView()
    {  
        // initComponents();
    }  
  
    public static void main(String ...args)
    {  
       /* 
        * // Run this later:
        SwingUtilities.invokeLater(new Runnable() 
        {  
            @Override
            public void run() 
            {  
                final JFrame frame = new JFrame();  
                 
                frame.getContentPane().add(new SwingFXWebView());  
                 
                frame.setMinimumSize(new Dimension(640, 480));  
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
                frame.setVisible(true);  
            }  
        }); 
        */  
    }  
     
    private void initComponents()
    {    
        jfxPanel = new JFXPanel();  
        createScene();  
         
        setLayout(new BorderLayout());  
        add(jfxPanel, BorderLayout.CENTER);  
    }     
     
    private void createScene() 
    {  
        PlatformImpl.startup(new Runnable() 
        {  
            @Override
            public void run() 
            {  
                stage = new Stage();  
                 
                stage.setTitle("Hello Java FX");  
                stage.setResizable(true);  
   
                Group root = new Group();  
                Scene scene = new Scene(root,80,20);  
                stage.setScene(scene);  
                 
                // Set up the embedded browser:
                browser = new WebView();
                webEngine = browser.getEngine();
                
                try 
                {
					BufferedWriter writer = new BufferedWriter(new FileWriter(new File("src/" + nameFile)));
					writer.write(contentHTML);
					writer.close();
					URL url1;
					url1 = (new java.io.File("src/" + nameFile)).toURI().toURL();
					webEngine.load(url1.toExternalForm());
				} 
                catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    
               ObservableList<Node> children = root.getChildren();
               children.add(browser);                     
                 
               jfxPanel.setScene(scene);  
            }  
        });  
    }
}