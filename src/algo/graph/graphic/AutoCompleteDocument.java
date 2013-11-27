package algo.graph.graphic;

 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import algo.graph.parsing.Stops;
 
public class AutoCompleteDocument extends PlainDocument 
{
	private static final long serialVersionUID = 1L;

	private List<String> dictionary = new ArrayList<String>();
 
	private final JTextComponent _textField;
	 
	public AutoCompleteDocument(JTextComponent field, List<Stops> aDictionary) 
	{
		_textField = field;
		
		for(Stops stop : aDictionary)
		{
			dictionary.add(stop.stop_name);
		}
	}
 
	public void addDictionaryEntry(String item) 
	{
		dictionary.add(item);
	}
 
	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException 
			{
		super.insertString(offs, str, a);
		String word = autoComplete(getText(0, getLength()));
		if (word != null) {
			super.insertString(offs + str.length(), word, a);
			_textField.setCaretPosition(offs + str.length());
			_textField.moveCaretPosition(getLength());
		}
	}
 
	public String autoComplete(String text) {
		for (Iterator<String> i = dictionary.iterator(); i.hasNext();) {
			String word = i.next();
			if (word.startsWith(text)) {
				return word.substring(text.length());
			}
		}
		return null;
	}
 

	public static JTextField createAutoCompleteTextField(List<Stops> dictionary) 
	{
		JTextField field = new JTextField(20);
		field.setPreferredSize(new Dimension(180, 20));
		field.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		AutoCompleteDocument doc = new AutoCompleteDocument(field, dictionary);
		field.setDocument(doc);
		return field;
	}
 
	/*public static void main(String[] args)
	{
		//String[] dict = { "Alef++", "alef++", "sourceForge", "SourceFORGE", "JAVA","JAVA2","PROGRAMMATION", "programmation", "Team" };
		JTextField field = AutoCompleteDocument.createAutoCompleteTextField(dict);
 
		JFrame frame = new JFrame("Autocomplete");
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel("Text Field: "));
		frame.add(field);
		frame.pack();
		frame.setVisible(true);
	}*/
}

