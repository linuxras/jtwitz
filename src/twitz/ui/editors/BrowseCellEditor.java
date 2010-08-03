/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.editors;

import java.awt.event.WindowEvent;
import twitz.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.event.CellEditorListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import twitz.ui.dialogs.TwitzOAuthDialog;
import twitz.util.DBManager;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author Andrew Williams
 */
public class BrowseCellEditor extends AbstractCellEditor implements TableCellEditor {
	private final TwitzSessionManager session;
	private final javax.swing.JDialog caller;

	public BrowseCellEditor(javax.swing.JDialog parent, String sessionName) {
		this.caller = parent;
		this.sessionName = sessionName;
		this.session = twitz.util.TwitzSessionManager.getInstance();
		initComponents();
		
	}

	private void initComponents() {//{{{

        txtPath = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
		btnShowDialog = new javax.swing.JButton();
		//logger.info("Session Name = "+this.sessionName);
		TwitzMainView view = session.getTwitzMainViewForSession(this.sessionName);
		componentEditor = new TwitzOAuthDialog(view.getMainFrame(), true, this.sessionName);
		componentEditor.setLocationRelativeTo(caller);
		WindowListener cListener = new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e)
			{
				//logger.info("WindowAdater did it----------------------------------------");
				stopCellEditing();
			}
		};
		componentEditor.addWindowListener(cListener);

		boolEditor.setAlignmentX(javax.swing.JSpinner.LEFT_ALIGNMENT);
		boolEditor.getEditor().setAlignmentX(javax.swing.JSpinner.LEFT_ALIGNMENT);

        //panel.setMinimumSize(new java.awt.Dimension(54, 12));
        panel.setName("Form"); // NOI18N

        resourceMap = TwitzApp.getContext().getResourceMap(BrowseCellEditor.class);
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
		btnShowDialog.setAction(actionMap.get("loadDialog")); // NOI18N
        btnShowDialog.setIcon(resourceMap.getIcon("btnShowDialog.icon")); // NOI18N
        btnShowDialog.setText(resourceMap.getString("btnShowDialog.text")); // NOI18N
		btnShowDialog.setToolTipText(resourceMap.getString("btnShowDialog.toolTipText")); // NOI18N
        btnShowDialog.setFocusable(false);
        btnShowDialog.setName("btnShowDialog"); // NOI18N

		int skinCount = resourceMap.getInteger("Substance.skins.count");
		skins = new String[skinCount];
		for(int i=0; i < skinCount; i++) {
			skins[i] = resourceMap.getString("Substance.skin["+ i +"]");
		}
		cmbSkins = new javax.swing.JComboBox(skins);
		cmbSkins.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				stopCellEditing();
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});
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
		chooser.setFileView(new ImageView());
		chooser.setAccessory(new ImagePreviewBox(chooser));
		int sv = chooser.showSaveDialog(panel);
        File sFile;
		if(sv == JFileChooser.APPROVE_OPTION)
        {
			sFile = chooser.getSelectedFile();
			txtPath.setText(sFile.getAbsolutePath());
		}
	}

	@Action
	public void loadDialog()
	{
		//this.componentEditor.setSelectedId(config.getInteger(DBManager.SESSION_TWITTER_OAUTH_ID));
		this.componentEditor.setMode(TwitzOAuthDialog.Mode.SELECT);
		this.componentEditor.setVisible(true);
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
				passEditor.setText(configVal);
//				txtString.setText(configVal);
			}
			else
			{
				passEditor.setText(cellVal);
//				txtString.setText(cellVal);
			}
			currentEditor = "Password";
			//return txtString;
			return passEditor;
		}
		else if(type.equalsIgnoreCase("component"))
		{
			if(cellVal.equals(configVal))
			{
				this.componentEditor.setSelectedId(config.getInteger(val));
			}
			else
			{
				int cv = Integer.parseInt(cellVal);
				this.componentEditor.setSelectedId(cv);
			}
			currentEditor = "Component";
			return this.btnShowDialog;
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
		else if(currentEditor.equalsIgnoreCase("Password")) {
			char[] pass = passEditor.getPassword();
			StringBuilder buf = new StringBuilder();
			for(int i=0; i < pass.length; i++)
				buf.append(pass[i]);
			//if(logdebug)
			//	logger.debug("Current Password String: "+buf.toString());
			rv = buf.toString();
		}
		else if(currentEditor.equalsIgnoreCase("Component"))
		{
			rv = this.componentEditor.getSelectedId()+"";
			//rv = (String)componentValue;
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
	public static final String SESSION_PROPERTY = "sessionChanged";
	private javax.swing.ActionMap actionMap = twitz.TwitzApp.getContext().getActionMap(BrowseCellEditor.class, this);
	private String sessionName = "Default";
    private javax.swing.JButton btnBrowse;
	private javax.swing.JButton btnShowDialog;
    private javax.swing.JTextField txtPath;
	private javax.swing.JTextField txtString = new javax.swing.JTextField();
	private javax.swing.JPanel panel = new javax.swing.JPanel();
	private javax.swing.JCheckBox chkEditor = new javax.swing.JCheckBox();
	private javax.swing.JPasswordField passEditor = new javax.swing.JPasswordField();
	private String[] skins;
	private javax.swing.JComboBox cmbSkins;
	//private javax.swing.JComboBox cmbSkins = new SubstanceSkinComboSelector();
	private String boolModel[] = {"true", "false"};
	private javax.swing.JSpinner boolEditor = new javax.swing.JSpinner(new javax.swing.SpinnerListModel(boolModel));
	private TwitzOAuthDialog componentEditor;// = new TwitzOAuthDialog(view, true);
	private twitz.util.SettingsManager config = twitz.util.TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);//twitz.util.SettingsManager.getInstance();
	private String currentEditor = null;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private boolean logdebug = logger.isDebugEnabled();
	org.jdesktop.application.ResourceMap resourceMap;
	private Object componentValue;
    // End of variables declaration

	class ImageFileFilter extends FileFilter
    {
        public boolean accept(File f)
        {
            if(f.isDirectory())
            {
                return true;
            }
            String ext = ImageFilterUtils.getFileExtension(f);
            if(ext != null)
            {
                if(ext.endsWith(ImageFilterUtils.jpg) ||
                        ext.equals(ImageFilterUtils.jpeg) ||
                        ext.equals(ImageFilterUtils.png) ||
						ext.equals(ImageFilterUtils.gif))
                {
                    return true;
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

	class ImageView extends FileView
	{
		ResourceMap res = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);

		public ImageView()
		{
			super();
		}

		@Override
		public String getName(File f)
		{
			return null;
		}

		@Override
		public String getDescription(File f)
		{
			return null;
		}

		@Override
		public Boolean isTraversable(File f)
		{
			return null;
		}

		@Override
		public String getTypeDescription(File f)
		{
			String extension = ImageFilterUtils.getFileExtension(f);
			String type = null;

			if (extension != null)
			{
				if (extension.equals(ImageFilterUtils.jpeg) || extension.equals(ImageFilterUtils.jpg))
				{
					type = "JPEG Image";
				}
				else if (extension.equals(ImageFilterUtils.gif))
				{
					type = "GIF Image";
				}
				else if (extension.equals(ImageFilterUtils.png))
				{
					type = "PNG Image";
				}
			}
			return type;
		}

		@Override
		public Icon getIcon(File f)
		{
			String extension = ImageFilterUtils.getFileExtension(f);
			Icon icon = null;

			if (extension != null)
			{
				if (extension.equals(ImageFilterUtils.jpeg)
						|| extension.equals(ImageFilterUtils.jpg))
				{
					icon = res.getIcon("icon.jpg");
				}
				else if (extension.equals(ImageFilterUtils.gif))
				{
					icon = res.getIcon("icon.gif");
				}
				else if (extension.equals(ImageFilterUtils.png))
				{
					icon = res.getIcon("icon.png");
				}
			}
			return icon;
		}

	}

	static class ImageFilterUtils
	{

		public static final String jpg = "jpg";
		public static final String jpeg = "jpeg";
		public static final String png = "png";
		public static final String gif = "gif";

		public static String getFileExtension(File f)
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

	class ImagePreviewBox extends javax.swing.JComponent implements java.beans.PropertyChangeListener
	{
		File imageFile = null;
		javax.swing.ImageIcon thumbImage = null;

		public ImagePreviewBox(JFileChooser chooser)
		{
			setPreferredSize(new java.awt.Dimension(100, 50));
			chooser.addPropertyChangeListener(this);
		}

		public void doImageLoad()
		{
			if (imageFile == null)
			{
				thumbImage = null;
				return;
			}
			ImageIcon preScaleIcon = new ImageIcon(imageFile.getPath());
			if (preScaleIcon != null)
			{
				if (preScaleIcon.getIconWidth() > 90)
				{
					thumbImage = new ImageIcon(preScaleIcon.getImage().	getScaledInstance(90, -1, java.awt.Image.SCALE_DEFAULT));
				}
				else
				{
					thumbImage = preScaleIcon;
				}
			}
		}

		public void propertyChange(java.beans.PropertyChangeEvent e)
		{
			boolean update = false;
			String pname = e.getPropertyName();

			//reset on directory detection
			if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(pname))
			{
				imageFile = null;
				update = true;
			}
			else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(pname))
			{
				imageFile = (File) e.getNewValue();
				update = true;
			}

			//Do the work of updating the thumbnail or clearing the view
			if (update)
			{
				thumbImage = null;
				if (isShowing())
				{
					doImageLoad();
					repaint();
				}
			}
		}

		@Override
		protected void paintComponent(java.awt.Graphics g)
		{
			if (thumbImage == null)
			{
				doImageLoad();
			}
			if (thumbImage != null)
			{
				int x = (getWidth() / 2) - (thumbImage.getIconWidth() / 2);
				int y = (getHeight() / 2) - (thumbImage.getIconHeight() / 2);

				if (y < 0)
				{
					y = 0;
				}

				if (x < 5)
				{
					x = 5;
				}
				thumbImage.paintIcon(this, g, x, y);
			}
		}
	}
}
