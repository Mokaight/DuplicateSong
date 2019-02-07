package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.io.FilenameUtils;
import Lecteur.VisualPlayer;
import Comparaison.AudioSimilarityChecker;
import Comparaison.WorkerThread;
import Classes.BackgroundPanel;
import Classes.CustomTableCellRenderer;
import Classes.Format;
import Classes.MonBouton;
import Classes.MonOptionFenetre;

public class LogicielVueControleur extends JFrame implements ActionListener {
	//variable test 
	VisualPlayer VP;
	// declaration des composants
	 JPanel fenetrePanel;
	 JPanel choix;
	 JPanel boutonChoix;
	 JPanel image;
	 JPanel boutonRes;
	 JPanel option;
	 JPanel resultat;
	 JPanel logo;
	 JPanel tempo;
	 Color couleurOfficiel;
	 Color couleurOfficiel2;
	 Font policeOfficiel;
	 ImageIcon iconOfficiel;
	 String osOfficiel;
	 //Lecteur MP3
	// MP3PlayerGUI dialog;
	 AudioSimilarityChecker ASC;
	// declaration composants de option
	JButton boutonChemin1;
	JLabel labelChemin1;
	JLabel affichageMessage;
	JTextField jtfChemin1;
    MonOptionFenetre mof;
    //Le chemin avec l'extension avec / a la fin 
    
	String strChemin1;
	String strCheminTmpSousDossier;
	
	ArrayList<String []> sdoub;
	ArrayList<File> fdoub;
	ArrayList<String> cdoub;
	
	//Le dernier chemin clicker dans le tableau gÃ©nÃ©rÃ© par l'arbre
	String cheminFocus;
	//Extension du fichier actuelle
	String extension;
	//Le chemin avec l'extension sans / a la fin
	String fichierExtension;
	File fileChemin1;
	
	ArrayList<String> listeChemin;
	
	//declaration composants de bouton
	JButton RemoveAll;
	JButton Recherche;
	JButton Supprimer;
	JButton SelectAll;
	JButton SupprimerDoub;
	JButton Rename;
	JButton OptimisationTaille;
	//Bouton du Lecteur mp3 ? 
	
	JButton Play;
	JButton Pause;
	JButton Stop;
	
	JCheckBox morph = new JCheckBox("Inclure sous dossiers");
	JCheckBox avance = new JCheckBox("Recherche Poussée");

	JTable tabComp;
	ArrayList<Integer> listeSelectDoub;
	CustomTableCellRenderer tcrDoub;
	int rowSupDoub = -1;
	//tableau de base
	private JTable tableau;
	ArrayList<Integer> listeSelect;
	CustomTableCellRenderer tcr;
	int rowSup = -1;
	
	//TEST//////////////////////
	LoadingBar JP;
	//tableau des fichiers 
	String [] listefichiers;
	//////////////////////////////////////Initialisation du dir ////////////////////////////////////////////////////:
	//avec un A juste pour qu'il soit définit;
	private File dir = new File("a");
	
	
	//Boolean permettant de savoir dans quel tableau on a cliqué 
	//utile pour les boutons clickable ou pas 
	public boolean tabClick = true; //si tableau 1 , false si tableau 2 
	
	
	public ArrayList<String> getlisteChemin(){
		return this.listeChemin;
	}
	public LogicielVueControleur() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{

	    initialisationFenetre();
		
	}
	
	public void initialisationFenetre() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
	    this.setTitle("SnouFDoublons");
	    this.setSize(800, 500);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //Définition de l'écouteur à l'aide d'une classe interne anonyme
	    this.addWindowListener(new WindowAdapter(){
	             public void windowClosing(WindowEvent e){
	                 if (JOptionPane.showConfirmDialog(fenetrePanel, 
	                         "Are you sure to close this window?", "Really Closing?", 
	                         JOptionPane.YES_NO_OPTION,
	                         JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
	                	 	 removeDirectory(dir);
	                         System.exit(0);
	                     }
	                   
	             }
	    });
	    if( System.getProperty("os.name").contains("Mac")){
	    	this.setMinimumSize(new Dimension(1000,400));
	    }
	    else{
	    	this.setMinimumSize(new Dimension(900,400));	
	    }
	    initialisationPanel();
	    this.setVisible(true);
	}
	
	public static void removeDirectory(File dir) {
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        if (files != null && files.length > 0) {
	            for (File aFile : files) {
	                removeDirectory(aFile);
	            }
	        }
	        dir.delete();
	    } else {
	        dir.delete();
	    }
	}
	public void reload(){
		this.validate();
	}
	
public void initialisationPanel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
couleurOfficiel = new Color(232,232,230);
		couleurOfficiel2 = new Color(118,118,118);
		policeOfficiel = new Font("DejaVu Sans Condensed", Font.BOLD, 14);
		iconOfficiel = new ImageIcon(LogicielVueControleur.class.getResource("/icon.png"));
		Border b= BorderFactory.createBevelBorder(BevelBorder.LOWERED,couleurOfficiel,couleurOfficiel2);
	    this.setIconImage(iconOfficiel.getImage());	    
	    osOfficiel = System.getProperty("os.name");
	    System.out.println("Bonjours ! Vous avez lancé ce logiciel sous : "+osOfficiel);
		
		// declaration des panels
	    fenetrePanel = new BackgroundPanel(LogicielVueControleur.class.getResource("/Back3.png"));
		this.setContentPane(fenetrePanel);
		
		choix = new JPanel();
		choix.setOpaque(false);
        choix.setBorder(b);
        
		boutonChoix = new JPanel();
		boutonChoix.setOpaque(false);
		
		image = new BackgroundPanel(LogicielVueControleur.class.getResource("/logo.png"));
		image.setOpaque(false);
		
		option = new BackgroundPanel(LogicielVueControleur.class.getResource("/fond2.png"));
		option.setBorder(b);

		
		boutonRes = new JPanel();
		boutonRes.setOpaque(false);
		
		resultat = new JPanel();
		resultat.setOpaque(false);
		resultat.setBorder(b);
		
		VP = new VisualPlayer();
		VP.getPanel().setOpaque(false);
		
		JP = new LoadingBar(50);
		JP.getPanneau().setOpaque(false);
		
		placementDesPanels();
		initialisationOption();
		initialisationBoutonResultat();
		initialisationResultat();
		initialisationChoix();
		initialisationLoading();
	}
	public void placementDesPanels(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
	//	JPanel lecteurMP3 = new JPanel();
		GridBagConstraints gbc_lecteurMP3 = new GridBagConstraints();
		gbc_lecteurMP3.gridwidth = 12;
		gbc_lecteurMP3.insets = new Insets(0, 0, 5, 5);
		gbc_lecteurMP3.fill = GridBagConstraints.BOTH;
		gbc_lecteurMP3.gridx = 0;
		gbc_lecteurMP3.gridy = 0;
		getContentPane().add(VP.getPanel(), gbc_lecteurMP3);
		
	//	JPanel option = new JPanel();
		GridBagConstraints gbc_option = new GridBagConstraints();
		gbc_option.insets = new Insets(0, 0, 5, 0);
		gbc_option.gridwidth = 2;
		gbc_option.fill = GridBagConstraints.BOTH;
		gbc_option.gridx = 12;
		gbc_option.gridy = 0;
		getContentPane().add(option, gbc_option);
		
	//	JPanel choix = new JPanel();
		GridBagConstraints gbc_choix = new GridBagConstraints();
		gbc_choix.gridwidth = 14;
		gbc_choix.insets = new Insets(0, 0, 5, 0);
		gbc_choix.fill = GridBagConstraints.BOTH;
		gbc_choix.gridx = 0;
		gbc_choix.gridy = 1;
		getContentPane().add(choix, gbc_choix);
		
	//	JPanel panel = new JPanel();
		GridBagConstraints gbc_loadingJP = new GridBagConstraints();
		gbc_loadingJP.gridwidth = 10;
		gbc_loadingJP.insets = new Insets(0, 0, 5, 5);
		gbc_loadingJP.fill = GridBagConstraints.BOTH;
		gbc_loadingJP.gridx = 0;
		gbc_loadingJP.gridy = 2;
		getContentPane().add(JP.getPanneau(), gbc_loadingJP);
		
	//	JPanel boutonChoix = new JPanel();
		GridBagConstraints gbc_boutonChoix = new GridBagConstraints();
		gbc_boutonChoix.gridwidth = 3;
		gbc_boutonChoix.insets = new Insets(0, 0, 5, 0);
		gbc_boutonChoix.fill = GridBagConstraints.BOTH;
		gbc_boutonChoix.gridx = 11;
		gbc_boutonChoix.gridy = 2;
		getContentPane().add(boutonChoix, gbc_boutonChoix);
		
	//	JPanel resultat = new JPanel();
		GridBagConstraints gbc_resultat = new GridBagConstraints();
		gbc_resultat.gridheight = 2;
		gbc_resultat.gridwidth = 14;
		gbc_resultat.insets = new Insets(0, 0, 5, 0);
		gbc_resultat.fill = GridBagConstraints.BOTH;
		gbc_resultat.gridx = 0;
		gbc_resultat.gridy = 3;
		getContentPane().add(resultat, gbc_resultat);
		
		//	JPanel panel = new JPanel();
		GridBagConstraints gbc_logo = new GridBagConstraints();
		gbc_logo.gridwidth = 10;
		gbc_logo.insets = new Insets(0, 0, 5, 5);
		gbc_logo.fill = GridBagConstraints.BOTH;
		gbc_logo.gridx = 0;
		gbc_logo.gridy = 5;
		getContentPane().add(image, gbc_logo);
		
	//	JPanel boutonRes = new JPanel();
		GridBagConstraints gbc_boutonRes = new GridBagConstraints();
		gbc_boutonRes.gridwidth = 3;
		gbc_boutonRes.fill = GridBagConstraints.BOTH;
		gbc_boutonRes.gridx = 11;
		gbc_boutonRes.gridy = 5;
		getContentPane().add(boutonRes, gbc_boutonRes);
	}
	public void initialisationResultat(){
		resultat.setLayout(new BorderLayout());
	}
	
	public void initialisationChoix(){
		choix.setLayout(new BorderLayout());
	}
	public void initialisationLoading(){
	    JP.getTexte().setFont(policeOfficiel);
		JP.getTexte().setForeground(couleurOfficiel);
	}
	public void initialisationOption(){
		JPanel parcourir= new JPanel();
		parcourir.setOpaque(false);
		listeChemin = new ArrayList<String>();
		boutonChemin1 = new MonBouton(LogicielVueControleur.class.getResource("/iconLoupe.png"),LogicielVueControleur.class.getResource("/iconLoupe2.png"));
		boutonChemin1.addActionListener(this);
		labelChemin1 = new JLabel("Chemin :");
		labelChemin1.setFont(policeOfficiel);
		labelChemin1.setForeground(new Color(78,78,78));
		fichierExtension = new String();	
		//morph.addActionListener(this);
		morph.setOpaque(false);
		morph.setFont(policeOfficiel);
		morph.setForeground(new Color(78,78,78));
		avance.setOpaque(false);
		avance.setFont(policeOfficiel);
		avance.setForeground(new Color(78,78,78));
		
		jtfChemin1 = new JTextField("AUCUN");
		jtfChemin1.setEnabled(false);
		jtfChemin1.setFont(policeOfficiel);
	    jtfChemin1.setPreferredSize(new Dimension(150, 30));
	    jtfChemin1.setForeground(couleurOfficiel2);
	    mof = new MonOptionFenetre(null, "Choix du Dossier", true);
	    affichageMessage = new JLabel();
	    image.add(affichageMessage);
		option.add(labelChemin1);
		parcourir.add(jtfChemin1);
		parcourir.add(boutonChemin1);
		option.add(parcourir);
		option.setLayout(new BoxLayout(option, BoxLayout.Y_AXIS));
		option.add(morph);

	}
	

	public void initialisationBoutonResultat(){
		ASC = new AudioSimilarityChecker();
		Recherche = new MonBouton(LogicielVueControleur.class.getResource("/buttonFind.png"),LogicielVueControleur.class.getResource("/buttonFind2.png"));
		Supprimer = new MonBouton(LogicielVueControleur.class.getResource("/buttonDelete.png"),LogicielVueControleur.class.getResource("/buttonDelete2.png"));
		RemoveAll = new MonBouton(LogicielVueControleur.class.getResource("/buttonRemoveAll.png"),LogicielVueControleur.class.getResource("/buttonRemoveAll2.png"));
		SelectAll = new MonBouton(LogicielVueControleur.class.getResource("/buttonSelectAll.png"),LogicielVueControleur.class.getResource("/buttonSelectAll2.png"));
		SupprimerDoub = new MonBouton(LogicielVueControleur.class.getResource("/buttonDelete.png"),LogicielVueControleur.class.getResource("/buttonDelete2.png"));
		Rename = new MonBouton(LogicielVueControleur.class.getResource("/buttonRename.png"),LogicielVueControleur.class.getResource("/buttonRename2.png"));
		OptimisationTaille = new MonBouton(LogicielVueControleur.class.getResource("/buttonSizeOpti.png"),LogicielVueControleur.class.getResource("/buttonSizeOpti2.png"));
		
		RemoveAll.addActionListener(this);
	    Recherche.addActionListener(this);
	    Supprimer.addActionListener(this);
	    SupprimerDoub.addActionListener(this);
		SelectAll.addActionListener(this);
		
		OptimisationTaille.addActionListener(this);
		Rename.addActionListener(this);
		boutonChoix.add(avance);
	    boutonChoix.add(Recherche);
		boutonChoix.add(RemoveAll);
		boutonChoix.add(SelectAll);
		boutonChoix.add(Supprimer);
		
		boutonRes.add(OptimisationTaille);
		boutonRes.add(Rename);
		boutonRes.add(SupprimerDoub);
	}
	
	public void listerRepertoire(File repertoire){ 

		//boolean sousFichier = true;
		
		boolean sousFichier = morph.isSelected();
		//System.out.println("LA SELECTION DES SOUS FICHIER EST 1/0 -> activer ou desactiver "+sousFichier);
		
		listeChemin.clear();

		fdoub = new ArrayList<>();
		sdoub = new ArrayList<>();
		cdoub = new ArrayList<>();

		int i; 
		listefichiers=repertoire.list(); 
		for(i=0;i<listefichiers.length;i++){ 
			if(Format.contains(FilenameUtils.getExtension(listefichiers[i]))){ //option selection
				//System.out.println(strChemin1+listefichiers[i].substring(0,listefichiers[i].length()-4));// on choisit la sous chaine - les 4 derniers caracteres ".wav"
		//	System.out.println(strChemin1+listefichiers[i]+"test2");
			listeChemin.add(strChemin1+listefichiers[i]);
			}
			else{
				File fTmp = new File(strChemin1+listefichiers[i]);
					if(fTmp.isDirectory() && sousFichier && !(fTmp.getName().equals("tmp"))){
						strCheminTmpSousDossier = strChemin1;
						addSousFichier(fTmp);
					}
					else{
						//System.out.println(" le fichier est pas un .wav -> "+listefichiers[i]);	
					}
			}
		} 
		
		makeListe(listefichiers,repertoire, this.choix);
		
	}
	
	public void addSousFichier(File fTmp){
		
		String [] lFichier;
		lFichier = fTmp.list();
		strCheminTmpSousDossier = strCheminTmpSousDossier + fTmp.getName()+"/";
		
		cdoub.add(strCheminTmpSousDossier);
		fdoub.add(fTmp);
		sdoub.add(lFichier);
		
		for(int i=0;i<lFichier.length;i++){ 
			if(Format.contains(FilenameUtils.getExtension(lFichier[i]))){
				System.out.println("ON AJOUTE AUSSI"+strChemin1+'@'+strCheminTmpSousDossier+"/"+'@'+lFichier[i]);
				listeChemin.add(strCheminTmpSousDossier+lFichier[i]);
			}
			else{
				File fTmp2 = new File(strCheminTmpSousDossier+lFichier[i]);
				//System.out.println("C FTMP2 BTARAD : " + strChemin1+strCheminTmpSousDossier+lFichier[i]);
				if(fTmp2.isDirectory()){
					System.out.println(strChemin1+strCheminTmpSousDossier+ "pourquoi c est dd"+lFichier[i]);
					addSousFichier(fTmp2);
					//System.out.println(strChemin1+strCheminTmpSousDossier+lFichier[i]);
				}
				/*else
					System.out.println(" le fichier est pas un .wav -> "+lFichier[i]);*/
		
			}
		}

		//makeListeDoublon(lFichier,fTmp);
		
	}
	
	public void makeListeDoublon(String[] listeRepertoire,File repertoire,String patchPlus){
		
		//System.out.println("COMMENT CA ??");
		
		System.out.println(listeRepertoire[0]+ "@@" +repertoire+ "@@" +patchPlus);
		
		File[] monFile = repertoire.listFiles();
		String[] tmpSplit  = new String[2];
		
		String decoupe = repertoire.toString();
		String decoupe2 = decoupe.substring(strChemin1.length());
		
		System.out.println(decoupe2+ " decoupe : ");
		
		for(int i = 0; i < listeRepertoire.length; i++){

			if (listeRepertoire[i].contains(".")) {
		    	
		    	
	            int q = listeRepertoire[i].lastIndexOf (".");
	            String extension = FilenameUtils.getExtension(listeRepertoire[i]);
	            if ( q != -1 )
	            {
	            	if(System.getProperty("os.name").contains("Windows")){

		            	tmpSplit[0] = decoupe2+'\\'+listeRepertoire[i].substring(0, q);
		            	tmpSplit[1] = extension;	
	            	}
	            	else{

		            	tmpSplit[0] = decoupe2+'/'+listeRepertoire[i].substring(0, q);
		            	tmpSplit[1] = extension;
		           
	            	}
	            }
		    	
		    	
		    //	System.out.println("les slpits : "+tmpSplit[0]+" et "+tmpSplit[1]);
			}
			else{
				String[] error = new String[2];
				error[0] = listeRepertoire[i];
				error[1] = "?";
				tmpSplit = error;
			}
			//N'affiche que les fichiers
	    	if (Format.contains(tmpSplit[1])){
	    		Object[] lineData = {tmpSplit[0], conversion(monFile[i].length()), tmpSplit[1]};
	    		((DefaultTableModel)tableau.getModel()).addRow(lineData);
	    	//	System.out.println(lineData[0] + " + " + lineData [1]);
	    	}
			
	    	
	    	
		}
		
	}
	
	public String conversion(long nb){
		
		String retour;
		
		if(nb == 0){
			return "";
		}
		
		if(nb / 1024 >= 2){
			nb = nb/1024;
			retour = nb+"ko";
		}
		else{
			retour = nb+"octet";
		}
		if(nb / 1024 >= 2){
			nb = nb/1024;
			retour = nb+"mo";
		}
		if(nb / 1024 >= 2){
			nb = nb/1024;
			retour = nb+"Go";
		}
		
		return retour;
	}
	
	
	public void makeListe(String[] listeRepertoire,File repertoire, JPanel choix) {

		//listeRep -> string de rep
		//rep -> rep courant
		
		File[] monFile = repertoire.listFiles();	//utililaire tmp de creation de Jtable
		String[] tmpSplit  = new String[2];	//utililaire tmp de creation de Jtable
		
		//*** Ajout de la premiere ligne du tableau / row = 1 ***//
			//Cela permet de declarer le tableau sans etre vide//
		
		if (listeRepertoire[0].contains(".")) {
            int q = listeRepertoire[0].lastIndexOf (".");
            String extension = FilenameUtils.getExtension(listeRepertoire[0]);
            if ( q != -1 )
            {
            	tmpSplit[0] = listeRepertoire[0].substring(0, q);
            	tmpSplit[1] = extension;
            }
	    	
	    	
	    	//System.out.println("les slpits : "+tmpSplit[0]+" et "+tmpSplit[1]);

		} else {
			String[] error = new String[2];
			error[0] = listeRepertoire[0];
			error[1] = "?";
			tmpSplit = error;
			//System.out.println("bug");
		}
		
		//*** Fin d'ajout ***///
		
		//**** Creation du tableau avec ses data, creation du customTablecellrenderer qui modifie la couleur des cellule dynamiquement ****//
		
		//System.out.println("lecture :"+tmpSplit[0]);
		
		Object[][] data = {{tmpSplit[0], conversion(monFile[0].length()), tmpSplit[1]}};	
	    String  title[] = {"Titre", "Taille", "Format"};
	    		tableau = new JTable(new DefaultTableModel(data, title)){public boolean isCellEditable(int row, int col) {return false;}};	//surcharge du constructeur JTable pour ne pas pouvoir modifier les cellules
	    		tcr = new CustomTableCellRenderer();
	    		listeSelect = new ArrayList<Integer>();	//voici la liste des numero de ligne selectionné
	    		tcr.setArrayList(listeSelect);	//on donne la liste de départ (vide) a notre modificateur de couleur
	    		tableau.setDefaultRenderer(Object.class, tcr);	//on donne le modificateur comme listener de notre tableau pour les changement couleur

	    //**** Association du tableau avec son listener ****//
	    try
            {
	    tableau.addMouseListener(new MouseAdapter() {
	    	  public void mouseClicked(MouseEvent e) {
	    	      //Degrise le bouton Rechercher qui n'a aucun sens dans le deuxieme tableau
	    	      tabClick = true;
	    	      if (tabClick == true){
	    	    	  ((MonBouton) Recherche).changeIcon(1);
	    	    	  Recherche.setEnabled(true);
	    	      }
	    	      
	    	    if (e.getClickCount() == 1) {	// si == 2 -> double clic
	    	      
	    	      JTable target = (JTable)e.getSource();
	    	      int row = -1;
	    	      row = target.getSelectedRow();
	    	      int column = target.getSelectedColumn();
	    	      
	    	      if ( row == -1){	//si on clic dans le vide du tableau -> zone blanche sans raw
	    	    	  //System.out.println(" LE CLIC N'EST PAS VALIDE");
	    	      }
	    	      else{	//so on clic bien sur un ligne
		    	     
		    	      //0 pour toujours prendre la premier case ( le nom du fichier ) 
		    	      String nomDuFichier = tableau.getModel().getValueAt(row,0).toString();
		    	      extension = tableau.getModel().getValueAt(row,2).toString();
		    	      fichierExtension = nomDuFichier + extension;
		    	      String absolutePath;
	
		    	      //Ligne de print de test
		    	    //  System.out.println( "Ceci est le print du Mouse Adapter de nomDuFichier "  + nomDuFichier);
		    	  //    System.out.println( "Ceci est le print du Mouse Adapter de extension "  + extension);
		    	      
		    	      //Si il y a une extension, alors on peut l'ajouter a la liste de musique ( ici on pourrait gerer le type d'extension 
		    	      // Sinon , on ne fait rien, on stock le cheminFocus quand mÃªme.
		    	      //A vérifier avec img et tout
		    	      if (Format.contains(extension) ){ 
		    	    	  absolutePath = strChemin1 + nomDuFichier;
		    	    	  cheminFocus = absolutePath +"."+extension;
		    	//    	  System.out.println( "Ceci est le print du Mouse Adapter de cheminFocus "  + cheminFocus);
		    	     
		    	    	  //Configuration du lecteur MP3 avec le Visual
		    	    	  lancerMP3(VP,cheminFocus,extension);
		    	    	  VP.getjPlayingActual().setText(nomDuFichier);
	     
		    	      
		    	    	  if (ASC.getUserMusics().contains(cheminFocus)) {	//problem de double nom -> exp de Apach dans download /// !!!!!!!!!!!!!!!!!!
		    	    	  	 ASC.removeUserMusics(cheminFocus);
		    	    	  	 ASC.removeDoblets();
		    	    	  	 //getSelectedRow().setBackground(Color.red);
		    	    	  	 // 	 System.out.println("Remove de " + cheminFocus);
		    	    	 // 	System.out.println("On sup la col "+row);
		    	    	  	listeSelect.remove((Integer)row);
		    	    	  	tcr.setArrayList(listeSelect);
		    	    	  	tableau.repaint();
		    	    	  } 
		    	    	  else {
		    	    		 ASC.addUserMusics(cheminFocus);
		    	    //		 System.out.print("Ajoute de " + cheminFocus);
			    	  //  	 System.out.println("On ajoute la col "+row);
		    	    		 listeSelect.add(row);
			    	    	 tcr.setArrayList(listeSelect);
			    	    	 tableau.repaint();
		    	    	  }
		    	      }
		    	      else{
		    	    	 absolutePath = strChemin1 + nomDuFichier;
			    	     cheminFocus = absolutePath; 
			    	     //System.out.println( "Ceci est le print du Mouse Adapter de cheminFocus quand  ? "  + cheminFocus);
		    	      }
		    	     
	    	      }
	    	      
	    	      tableau.getSelectionModel().clearSelection();	//permet de deselect la ligne -> evite les bug de table a moitier vide
	    	  
	    	    }
	    	  }
	    	});
	 
	    
	    TableColumn col = tableau.getColumnModel().getColumn(0);
        col.setPreferredWidth(200);

	    for(int i = 1; i < listeRepertoire.length; i++){

			if (listeRepertoire[i].contains(".")) {
		    	
		    	
	            int q = listeRepertoire[i].lastIndexOf (".");
	            String extension = FilenameUtils.getExtension(listeRepertoire[i]);
	            if ( q != -1 )
	            {
	            	tmpSplit[0] = listeRepertoire[i].substring(0, q);
	            	tmpSplit[1] = extension;
	            }
		    	
		    	
		    //	System.out.println("les slpits : "+tmpSplit[0]+" et "+tmpSplit[1]);
			}
			else{
				String[] error = new String[2];
				error[0] = listeRepertoire[i];
				error[1] = "?";
				tmpSplit = error;
			}
			//N'affiche que les fichiers
	    	if (Format.contains(tmpSplit[1])){
	    		Object[] lineData = {tmpSplit[0], conversion(monFile[i].length()), tmpSplit[1]};
	    		((DefaultTableModel)tableau.getModel()).addRow(lineData);
	    	//	System.out.println(lineData[0] + " + " + lineData [1]);
	    	}
			
	    	
	    	
		}


	    tableau.setPreferredScrollableViewportSize(tableau.getPreferredSize());
	    tableau.setFillsViewportHeight(true);
	    
	    // pour les doublons
	    
	    for(int r=0;r < fdoub.size();r++){
	    	makeListeDoublon(sdoub.get(r),fdoub.get(r),cdoub.get(r));
	    	System.out.println("---> "+sdoub.get(r)+" "+fdoub.get(r)+" "+cdoub.get(r));
	    }
	    
	   
	    //tableau.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

	    //System.out.println("hug");
	    //tableau.repaint();
	    JScrollPane sp = new JScrollPane(tableau);
	    //sp.getViewport().setBackground(Color.yellow);
	    //sp.setVerticalScrollBarPolicy(
	    		   //JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
	    
	    //Nous ajoutons notre tableau \E0 notre contentPane dans un scroll
	    //Sinon les titres des colonnes ne s'afficheront pas !
	    choix.removeAll();
	    choix.add(sp,BorderLayout.CENTER);
            }
            catch(NullPointerException e)
            {
                popUpError("Aucun fichier selectionné ! ");
            }
	    //choix.repaint();
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		LogicielVueControleur lvc = new LogicielVueControleur();
	}
	public void creerTableauDoublons(AudioSimilarityChecker ASC){
        resultat.removeAll();
        ArrayList<String> as = new ArrayList<String>();
        
        
        
        as = ASC.getRes();
        
        //System.out.println(as+" ICIIIIIIIIIIIIIIIIIIIIIIIIIIIIII "+strChemin1);
        
        //as.add("test");
        //as.add("test2");
        
        DefaultTableModel model = new DefaultTableModel();
        tabComp = new JTable(model){public boolean isCellEditable(int row, int col) {return false;}};
        //tabComp = new JTable(model); 
        
	    		
	    		tcrDoub = new CustomTableCellRenderer();
	    		listeSelectDoub = new ArrayList<Integer>();	//voici la liste des numero de ligne selectionné
	    		tcrDoub.setArrayList(listeSelectDoub);	//on donne la liste de départ (vide) a notre modificateur de couleur
	    		tabComp.setDefaultRenderer(Object.class, tcrDoub);	//on donne le modificateur comme listener de notre tableau pour les changement couleur


	    //**** Association du tableau avec son listener ****//
	    try
            {
	    tabComp.addMouseListener(new MouseAdapter() {
	    	  public void mouseClicked(MouseEvent e) {
	    		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Listener entréé!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    		//Click 2 fois dessus   
	    		/*if(e.getClickCount() == 2){
	    			System.out.println("totototonow");
	    		     int row = tabComp.getSelectedRow();
	    		      int col = tabComp.getSelectedColumn();
	    		      String nomDuFichier = tabComp.getModel().getValueAt(row,col).toString();
	    		      String absolutePath = strChemin1 + nomDuFichier;
	    			  tabComp.editCellAt(row, col);
	    			    JTextField field = (JTextField) ((DefaultCellEditor) tabComp
	    			            .getCellEditor()).getComponent();
	    			    field.requestFocus();
	    			    field.setSelectionStart(0);
	    			    int endSelection = 
	    			              (!field.getText().isEmpty()) ? field.getText().length() -1 : 0;
	    			    field.setSelectionEnd(endSelection);
	    			    System.out.println("Le field de la cell éditable " + field.toString());
	    		}*/
	    		
	    		//Click une fois dessus 
	    	    if (e.getClickCount() == 1) {	// si == 2 -> double clic
	    	      
	    	      //Grise le bouton Rechercher qui n'a aucun sens dans le deuxieme tableau
	    	      tabClick = false;
	    	      if (tabClick == false){
	    	    	  ((MonBouton) Recherche).changeIcon(2);
	    	    	  Recherche.setEnabled(false);
	    	      }
	    	      
	    	      JTable target = (JTable)e.getSource();
	    	      int row = -1;
	    	      row = target.getSelectedRow();
	    	      int column = target.getSelectedColumn();
	    	      
	    	      if ( row == -1){	//si on clic dans le vide du tabComp -> zone blanche sans raw
	    	    	  //System.out.println(" LE CLIC N'EST PAS VALIDE");
	    	      }
	    	      else{	//so on clic bien sur un ligne
		    	     
	    	    	  String nomDuFichier = tabComp.getModel().getValueAt(row,column).toString();
		    	      extension =FilenameUtils.getExtension(nomDuFichier);
		    	      String absolutePath;
		    	      //Ligne de print de test
		   // 	      System.out.println( "Ceci est le print du Mouse Adapter de nomDuFichier "  + nomDuFichier);
		    //	      System.out.println( "Ceci est le print du Mouse Adapter de extension "  + extension);
		    	      
		    	      //Si il y a une extension, alors on peut l'ajouter a la liste de musique ( ici on pourrait gerer le type d'extension 
		    	      // Sinon , on ne fait rien, on stock le cheminFocus quand mÃªme.
		    	      //A vérifier avec img et tout
		    	      if (Format.contains(extension) ){ 
		    	    	  absolutePath = strChemin1 + nomDuFichier;
		    	    	  //Juste l'absolute ici car nomDuFichier contient déja l'exntension;
		    	    	  cheminFocus = absolutePath;
		    	//    	  System.out.println( "Ceci est le print du Mouse Adapter de cheminFocus "  + cheminFocus);
		    	     
		    	    	  //Configuration du lecteur MP3 avec le Visual
		    	    	  lancerMP3(VP,cheminFocus,extension);
		    	    	  VP.getjPlayingActual().setText(nomDuFichier);

		    	      }
		    	      else{
		    	    	 absolutePath = strChemin1 + nomDuFichier;
			    	     cheminFocus = absolutePath; 
			    	//     System.out.println( "Ceci est le print du Mouse Adapter de cheminFocus quand  ? "  + cheminFocus);
		    	      }
		    	      //rowSupDoub = row;
		    	      
		    	      if(listeSelectDoub.contains((Integer)row)){
		    	    	  System.out.println("Remove de " + row);
		    	    	  	listeSelectDoub.remove((Integer)row);
		    	    	  	tcrDoub.setArrayList(listeSelectDoub);
		    	      }else{
		    	    	  System.out.println("ajout de " + row);
		    	    	  	listeSelectDoub.add(row);
		    	    	  	tcrDoub.setArrayList(listeSelectDoub);
		    	      }
		    	      tableau.repaint();
		    	     
	    	      }
	    	      
	    	      tabComp.getSelectionModel().clearSelection();	//permet de deselect la ligne -> evite les bug de table a moitier vide
	    	  
	    	    }
	    	  
	    	  }
	    	  
	    	});
        }
        // Create a couple of columns 
       catch(NullPointerException e)
       {
           popUpError("aucun fichier selectionné");
       }
        model.addColumn("Musique"); 
        model.addColumn("Doublons"); 


        //Remplissage du tableau
        for(int i=0;i<as.size();i = i +2){
        	
        	int taille = strChemin1.length();
        	
        	//System.out.println(as.get(i)+" as "+as.get(i+1));
        	
        	String a1 = as.get(i).substring(taille);
        	String a2 = as.get(i+1).substring(taille);
        	model.addRow(new Object[]{a1,a2 });
        	
        	//System.out.println(a1+" la "+a2);
        	
        	
        	//String a1 = as.get(i).substring(as.get(i).lastIndexOf('/') + 1);
        	//String a2 = as.get(i+1).substring(as.get(i+1).lastIndexOf('/') + 1);
        	//model.addRow(new Object[]{a1,a2 });
        }
        
	    JScrollPane sp = new JScrollPane(tabComp);
	    sp.getViewport().setBackground(couleurOfficiel2);
	    resultat.add(sp,BorderLayout.CENTER);
        repaint();
        ASC.emptyDoblets();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == boutonChemin1){
			mof.setVisibleuh();
			strChemin1 = mof.getString();
			System.out.println(strChemin1);
			jtfChemin1.setText(strChemin1);
			if(strChemin1 != ""){
				fileChemin1 = new File(strChemin1);
				//ecritureFichierSauv(strChemin1);
				if(fileChemin1.isDirectory()){
					listerRepertoire(fileChemin1);
					//System.out.println(strChemin1+"est");
				}
				else{
					
					//System.out.println("BIPPPPPPP non !");
				}
			}
			
		}

		//La transcription de byte [] en String[] est a revoir
		if(e.getSource() == Recherche){
			boolean selected = avance.isSelected();
			System.out.println("bouton Recherche");
    		/////////////////////////Création du dossier temp/////////////////////////////////////////
			 if (!dir.isDirectory()){
		     dir = new File(strChemin1 + "tmp");
			 dir.mkdir();
			 }
			 boutonChemin1.setEnabled(false);
			 Recherche.setEnabled(false);
            //launch doblets analysis
			if (selected == true){
				if(JOptionPane.showConfirmDialog(fenetrePanel, 
                        "La recherche avancée risque de prendre du temps", "Voulez vous la lancer?", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
				{
					updateBouton(ASC, JP,selected);
                }
				else {
					Recherche.setEnabled(true);
					avance.setSelected(false);
				}
			}
			else {
				updateBouton(ASC, JP,selected);
			}



            
		}

		if(e.getSource() == Supprimer){
			System.out.println("bouton Supprimer");
			System.out.println(fileChemin1);
			for(int i=0;i<ASC.getUserMusics().size();i++){
				File temp =  new File(ASC.getUserMusics().get(i));
				try
                                        {
                                            temp.delete();
                                            affichageMessage.setText("Musique" + fichierExtension + " a bien Ã©tÃ© supprimÃ©");
                                            //tableau.remove(rowSup);
                                        }
                                        catch(NullPointerException es)
                                        {
                                            popUpError("Aucun fichier a supprimer");
                                        }
			}
			try
                        {
                            ArrayList<Integer> ai = tcr.getListe();
                            Collections.sort(ai, Collections.reverseOrder());

                            for(int i=0; i < ai.size();i++){
                            		
                                    System.out.println("on supprime la ligne "+ai.get(i));
                                    ((DefaultTableModel)tableau.getModel()).removeRow(ai.get(i));
                            }

                            tableau.repaint();
                            tcr.removeListe();
                            repaint();

                            ASC.getUserMusics().clear();
                            cheminFocus = null;
                        }
                        catch(NullPointerException es)
                        {
                            popUpError("Auncun fichier a supprimer");
                        }
			
		}
		if(e.getSource() == Rename){
			if (!tcrDoub.getListe().isEmpty()){
				
			
				ArrayList<Integer> ai = tcrDoub.getListe();
	
				for(int i=0; i < ai.size();i++){
				String nomDuFichier = tabComp.getModel().getValueAt(ai.get(i),1).toString();
				File filebase = new File (strChemin1 + nomDuFichier);
		        String newValue = JOptionPane.showInputDialog(tabComp,
		                "Enter a new value:", tabComp.getValueAt(ai.get(i), 1));
		        ((DefaultTableModel) tabComp.getModel()).setValueAt(
		                                               newValue, ai.get(i), 1);
		        File rename = new File(strChemin1 + newValue);
		        boolean success = filebase.renameTo(rename);
		        if(!success){
		        	System.out.println("Erreur dans le renommage");
		        }
		        	majTabComp(nomDuFichier,newValue);
				}
			}	
		}
		if(e.getSource() == OptimisationTaille){
                    try
                        {
			//System.out.println("Click de Opti");
			if (!tcrDoub.getListe().isEmpty()){
				ArrayList<Integer> ai = tcrDoub.getListe();
	
				for(int i=0; i < ai.size();i++){
					String nomDuFichier = tabComp.getModel().getValueAt(ai.get(i),1).toString();
					String nomDuFichier2 = tabComp.getModel().getValueAt(ai.get(i),0).toString();
					System.out.println(nomDuFichier);
					System.out.println(nomDuFichier2);
					File temp1 = new File(strChemin1 + nomDuFichier);
					File temp2 = new File(strChemin1 + nomDuFichier2);
					if (temp1.length() > temp2.length()){
					((DefaultTableModel)tabComp.getModel()).removeRow(ai.get(i));
						if(temp1.delete()){
							System.out.println("Musique" + nomDuFichier + " a bien été supprimée");
							SuppressionLigneTableau(tableau, nomDuFichier,0);
							//tableau.remove(rowSup);
						}
						else{
							System.out.println("Erreur de suppression");
							
						}	
					}
					else{
						((DefaultTableModel)tabComp.getModel()).removeRow(ai.get(i));
						if(temp2.delete()){
							System.out.println("Musique" + nomDuFichier2 + " a bien été supprimée");
							SuppressionLigneTableau(tableau, nomDuFichier2,0);
							//tableau.remove(rowSup);
						}
						else{
							System.out.println("Erreur de suppression dans Opti");
						}	
	
					}
				}
                            }
                        }
                        catch(NullPointerException es)
                        {
                              popUpError("Auncun fichier a supprimer");
                        }
        			for (int j= 0 ; j < listeChemin.size() ; j ++){
        				if ((ASC.getUserMusics().contains(listeChemin.get(j)))) {	
        					ASC.removeUserMusics(listeChemin.get(j));
        					
        				}
        			}

        			try
                                {
                                    listeSelect.clear();
                                    tcr.setArrayList(listeSelect);
                                    tableau.repaint();
                                }
                                catch(NullPointerException es)
                                {
                                     popUpError("aucun fichier a remove");
                                }
        		
		}
		if(e.getSource() == SupprimerDoub){
			try
                        {
				SuppressionDoublons(tcrDoub,1);
                        }
			catch(NullPointerException es)
                        {
                               popUpError("erreur lors de la suppression"); 
                        }
				
	
			
		}
		//Le bouton est a  griser avant l'utilisation du jtable
		if (e.getSource() == SelectAll){
			for (int j= 0 ; j < listeChemin.size() ; j ++){
				if (!(ASC.getUserMusics().contains(listeChemin.get(j)))) {	
					ASC.addUserMusics(listeChemin.get(j));
					
					System.out.println(" fichiers : " + listeChemin.get(j).toString());
				}
				
			}
			try
                        {
                             for (int i=0;i<tableau.getRowCount(); i ++)
                             {
                            	 listeSelect.add(i);
                             }		
                            tcr.setArrayList(listeSelect);
                            tableau.repaint();
                        }
                        catch(NullPointerException es)
                        {
                            popUpError("aucun fichier a selectionner");
                        }
			
		}
		if(e.getSource() == RemoveAll){
			for (int j= 0 ; j < listeChemin.size() ; j ++){
				if ((ASC.getUserMusics().contains(listeChemin.get(j)))) {	
					ASC.removeUserMusics(listeChemin.get(j));
					
				}
			}

			try
                        {
                            listeSelect.clear();
                            tcr.setArrayList(listeSelect);
                            tableau.repaint();
                        }
                        catch(NullPointerException es)
                        {
                             popUpError("aucun fichier a remove");
                        }
            
		}

		try
                {
                    this.validate();
                }
                catch(IndexOutOfBoundsException es)
                {
                    popUpError("mistakes were made");
                }
		
	}
	
	public void majTabComp(String g,String r){
		
		System.out.println(" tab com : "+tabComp.getRowCount());
		
		for(int i = 0;i<tabComp.getRowCount();i++){
			System.out.println(tabComp.getValueAt(i, 1).toString());
			if(tabComp.getValueAt(i, 1).toString().equals(g))
				tabComp.setValueAt(r,i, 1);
		}

	}

	public void lancerMP3 (VisualPlayer VP, String path, String extension){
		if(!cheminFocus.isEmpty()){
	    VP.setFile(cheminFocus);
	    VP.setExtension(extension);
	    //System.out.println("LE CHEMIN DU VP est : " + VP.getFile() + " L'extension du VP est : " + VP.getExtension());
		}    
	}
	public void SuppressionDoublons (CustomTableCellRenderer tabDoub, int a){
		ArrayList<Integer> ai = tabDoub.getListe();
		Collections.sort(ai, Collections.reverseOrder());
		
		for(int i=0; i < ai.size();i++){
			String nomDuFichier = tabComp.getModel().getValueAt(ai.get(i),a).toString();
			System.out.println(nomDuFichier);
			System.out.println("on supprime la ligne "+ai.get(i));
			((DefaultTableModel)tabComp.getModel()).removeRow(ai.get(i));
			File temp =  new File(strChemin1 + nomDuFichier);
			if(temp.delete()){
				System.out.println("Musique" + fichierExtension + " a bien été supprimée");
				SuppressionLigneTableau(tableau,nomDuFichier,a);
				//tableau.remove(rowSup);
			}
			else{
				System.out.println("Erreur de suppression");
			}
			System.out.println("NOMBRE DE LIGNE DU TAB : " + tabComp.getRowCount());
			for (int j=0; j <tabComp.getRowCount() ; j++){
				System.out.println("Val de j : "+ j );
				System.out.println("Dans le bouclejdoaedajzoidhé");
				String nom2 = tabComp.getModel().getValueAt(ai.get(j),a).toString();
				System.out.println(nom2);
				
				
		   }
		}
		
		tabDoub.removeListe();
		cheminFocus = null;
		listerRepertoire(fileChemin1);
	}
	public void SuppressionLigneTableau (JTable tableau, String a, int col){
		String lesSplits;
		lesSplits = a.substring(0, a.lastIndexOf("."));
		System.out.println(" substring : " + lesSplits );
		for (int i=0; i< tableau.getRowCount() ; i++){
			if (tableau.getModel().getValueAt(i,col).toString().equals(lesSplits)){
				((DefaultTableModel)tableau.getModel()).removeRow(i);
			}
		}
		//suppression de l'instance dans Liste chemin
		listeChemin.remove(a);
		if(!ASC.getUserMusics().isEmpty()){
			ASC.removeUserMusics(a);
		}
		
		tableau.repaint();
		System.out.println("Bien repaint \n");
	
	}

    public  void updateBouton(AudioSimilarityChecker ASC, LoadingBar JP,boolean a){
      //On crée un Worker générique, cette fois
        ExecutorService tpes =
                Executors.newFixedThreadPool(20);
      SwingWorker sw = new SwingWorker<Integer, Integer>(){
        protected Integer doInBackground() throws Exception {
          int i;
          if(!ASC.getAED().gettestMusicSw().isEmpty()){
        	  ASC.getAED().clearTestMusicSw();
          }
          for(i = 0; i < ASC.getUserMusics().size(); i++){
           try {
                //System.out.println("Value de la JPROGRESS : " + i*100/(ASC.getUserMusics().size()));
            	Future<String> future = tpes.submit(new WorkerThread(ASC.getAED(),ASC.getUserMusics().get(i),strChemin1));
            	try {
            		ASC.getAED().addTestMusicSw(future.get());
    			} catch (InterruptedException e) {
    				popUpError("erreur inatendue");
    			} catch (ExecutionException e) {
    				popUpError("erreur innatendue");
    			}
              //On change la propriété d'état
              setProgress(i);
              //On publie un résultat intermédiaire 
              publish((i*100)/ASC.getUserMusics().size());
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              popUpError("interrupted !");
            }               
          }
          return i;
        }

        public void done(){
          if(SwingUtilities.isEventDispatchThread())
           
          //On utilise la méthode get() pour récupérer le résultat
            //de la méthode doInBackground()
            //C'est la fin
        	JP.renameText("Comparaison des morceaux");
            JP.getJprogressBar().setValue(100);

            ASC.setMusics(ASC.getAED().gettestMusicSw());
            if(a ==true){
                ASC.findDobletsAvance(ASC.getUserMusics(), ASC.getMusics(),listeChemin, strChemin1);

            }
            else{
               ASC.findDoblets(ASC.getUserMusics(), ASC.getMusics(),listeChemin, strChemin1);

            }
            JP.renameText("Fin du traitement");
		    creerTableauDoublons(ASC);
		    boutonChemin1.setEnabled(true);
		    Recherche.setEnabled(true);
        }   
        //La méthode gérant les résultats intermédiaires
        public void process(List<Integer> list){
          for(Integer str : list){
            System.out.println(str);
            JP.renameText("En cours de traitement");
            JP.getJprogressBar().setValue(str);
          }
        }
      };
      //On écoute le changement de valeur pour la propriété
      sw.addPropertyChangeListener(new PropertyChangeListener(){
        //Méthode de l'interface
        public void propertyChange(PropertyChangeEvent event) {
          //On vérifie tout de même le nom de la propriété
          if("progress".equals(event.getPropertyName())){
            if(SwingUtilities.isEventDispatchThread())
              //System.out.println("Dans le listener donc dans l'EDT ! ");
            //On récupère sa nouvelle valeur
            System.out.println(event.getPropertyName());
          }            
        }         
      });
      //On lance le SwingWorker
      sw.execute();
    }
    
    public void popUpError(String erreur)
    {
        JOptionPane jop3 = new JOptionPane();
        JOptionPane.showMessageDialog(null, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
} 