/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.EventObject;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.CellEditorListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import org.jdesktop.application.Action;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SkinInfo;

/**
 *
 * @author mistik1
 */
public class BrowseCellEditor extends AbstractCellEditor implements TableCellEditor {

	public BrowseCellEditor(/*twitz.util.SettingsManager c*/) {
		//config = c;
		cmbSkins.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				stopCellEditing();
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});
		initComponents();
	}

	private void initComponents() {//{{{

        txtPath = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
		boolEditor.setAlignmentX(javax.swing.JSpinner.LEFT_ALIGNMENT);
		boolEditor.getEditor().setAlignmentX(javax.swing.JSpinner.LEFT_ALIGNMENT);

        //panel.setMinimumSize(new java.awt.Dimension(54, 12));
        panel.setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = TwitzApp.getContext().getResourceMap(BrowseCellEditor.class);
        txtPath.setText(resourceMap.getString("txtPath.text")); // NOI18N
        txtPath.setName("txtPath"); // NOI18N
        txtPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPathActionPerformed(evt);
            }
        });
		txtPath.addKeyListener(new java.awt.event.KeyListener() {

			public void keyTyped(KeyEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					stopCellEditing();
				}
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void keyReleased(KeyEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});
		txtPath.setMinimumSize(new java.awt.Dimension(30, 12));

        btnBrowse.setIcon(resourceMap.getIcon("btnBrowse.icon")); // NOI18N
        btnBrowse.setText(resourceMap.getString("btnBrowse.text")); // NOI18N
		btnBrowse.setToolTipText(resourceMap.getString("btnBrowse.toolTipText")); // NOI18N
        btnBrowse.setMargin(new java.awt.Insets(2, 8, 2, 8));
		btnBrowse.setMinimumSize(new java.awt.Dimension(22, 12));
		btnBrowse.setPreferredSize(new java.awt.Dimension(30, 14));
        btnBrowse.setName("btnBrowse"); // NOI18N
        btnBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBrowseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(txtPath, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBrowse))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnBrowse)
                .addComponent(txtPath, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }//}}}

	private void txtPathActionPerformed(java.awt.event.ActionEvent evt)
	{
		// TODO add your handling code here:
	}

	private void btnBrowseMouseClicked(java.awt.event.MouseEvent evt)
	{
		browseFiles();
	}

	public void setText(String value) {
		txtPath.setText(value);
	}

	public String getText() {
		return txtPath.getText();
	}

	public javax.swing.JButton getButton() {
		return btnBrowse;
	}

	public javax.swing.JTextField getField() {
		return txtPath;
	}

	@Action
	public void browseFiles()
	{
		JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new ImageFileFilter());
        chooser.setAcceptAllFileFilterUsed(false);
		int sv = chooser.showSaveDialog(panel);
        File sFile;
		if(sv == JFileChooser.APPROVE_OPTION)
        {
			sFile = chooser.getSelectedFile();
			txtPath.setText(sFile.getAbsolutePath());
		}
	}

	public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column)//{{{
	{
		//Get the row data type
		TableModel model = table.getModel();
		String val = model.getValueAt(table.convertRowIndexToModel(row), 0).toString();
		String type = config.getString(val+".cfgtype");
		String cellVal = table.getValueAt(row, column)+"";
		String configVal = config.getString(val);
		//System.out.println("Row: "+row+" - Column: "+column+" - Item: "+val+" - Type: "+type);
		if(type.equalsIgnoreCase("File")) {
			if(cellVal.equals(configVal)) {
				txtPath.setText(configVal);
			}
			else {
				txtPath.setText(cellVal);
			}
			currentEditor = "File";
			return panel;
		}
		else if(type.equalsIgnoreCase("Boolean")) {
			//chkEditor.setSelected(config.getBoolean(val));
			if(cellVal.equals(configVal)) {
				boolEditor.setValue(configVal);
			}
			else
			{
				boolEditor.setValue(cellVal);
			}
			currentEditor = "Boolean";
			return boolEditor;
		}
		else if(type.equalsIgnoreCase("Theme")) {
			if(cellVal.equals(configVal)) {
				cmbSkins.setSelectedItem(configVal);
			}
			else
			{
				cmbSkins.setSelectedItem(cellVal);
			}
			currentEditor = "Theme";
			return cmbSkins;
		}
		else if(type.equalsIgnoreCase("password")) {
			if(cellVal.equals(configVal)) {
				txtString.setText(configVal);
			}
			else
			{
				txtString.setText(cellVal);
			}
			currentEditor = "Text";
			return txtString;
		}
		if(cellVal.equals(configVal)) {
			txtString.setText(configVal);
		}
		else
		{
			txtString.setText(cellVal);
		}
		currentEditor = "Text";
		return txtString;

	}//}}}

	public Object getCellEditorValue()//{{{
	{
		String rv = null;
		if(currentEditor.equals("File")) {
			rv = txtPath.getText();
		}
		else if(currentEditor.equals("Boolean")) {
			rv = boolEditor.getValue().toString();
		}
		else if (currentEditor.equals("Theme")) {
			rv = (String)cmbSkins.getSelectedItem();
		}
		else {
			rv = txtString.getText();
		}
		return rv;
	}//}}}

	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
//		if(anEvent instanceof java.awt.event.MouseEvent) {
//			return ((java.awt.event.MouseEvent)anEvent).getClickCount() >= 1;
//		}
//
//		return false;
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		return true;
	}

	@Override
	public boolean stopCellEditing()
	{
		super.fireEditingStopped();
		return true;
	}

	@Override
	public void cancelCellEditing()
	{
		super.fireEditingCanceled();
	}

    
	@Override
	public void addCellEditorListener(CellEditorListener l)
	{
		super.addCellEditorListener(l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l)
	{
		super.removeCellEditorListener(l);
	}

	// Variables declaration - do not modify
    private javax.swing.JButton btnBrowse;
    private javax.swing.JTextField txtPath;
	private javax.swing.JTextField txtString = new javax.swing.JTextField();
	private javax.swing.JPanel panel = new javax.swing.JPanel();
	private javax.swing.JCheckBox chkEditor = new javax.swing.JCheckBox();
	private String[] skins =
	{
		"Autumn", "BusinessBlackSteel", "BusinessBlueSteel", "Business", 
		"ChallengerDeep", "CremeCoffee", "Creme", "DustCoffee", "Dust",
		"EmeraldDusk", "Gemini", "GraphiteAqua", "GraphiteGlass", "Graphite", 
		"Magellan", "MistAqua", "MistSilver", "Moderate", "NebulaBrickWall",
		"Nebula", "OfficeBlue2007", "OfficeSilver2007", "Raven", "Sahara", "Twilight"
	};
	private javax.swing.JComboBox cmbSkins = new javax.swing.JComboBox(skins);
	//private javax.swing.JComboBox cmbSkins = new SubstanceSkinComboSelector();
	private String boolModel[] = {"true", "false"};
	private javax.swing.JSpinner boolEditor = new javax.swing.JSpinner(new javax.swing.SpinnerListModel(boolModel));
	private twitz.util.SettingsManager config = twitz.util.SettingsManager.getInstance();
	private String currentEditor = null;
    // End of variables declaration

	class ImageFileFilter extends FileFilter
    {
        public boolean accept(File f)
        {
            if(f.isDirectory())
            {
                return true;
            }
            String extension = new ImageFilterUtils().getFileExtension(f);
            if(extension != null)
            {
                if(extension.endsWith(ImageFilterUtils.jpg) ||
                        extension.equals(ImageFilterUtils.jpeg) ||
                        extension.equals(ImageFilterUtils.png) ||
						extension.equals(ImageFilterUtils.gif))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }

        public String getDescription()
        {
			String images = "Image files: .jpg, .jpeg, .png, .gif";
            return images;
        }
    }

	static class ImageFilterUtils
	{

		public static final String jpg = "jpg";
		public static final String jpeg = "jpeg";
		public static final String png = "png";
		public static final String gif = "gif";

		public String getFileExtension(File f)
		{
			String ext = null;
			String str = f.getName();
			int ind = str.lastIndexOf('.');

			if (ind > 0 && ind < str.length() - 1)
			{
				ext = str.substring(ind + 1).toLowerCase();
			}
			return ext;
		}
	}
}
