

 package nodepad;


 import java.awt.*;


 import java.awt.event.*;


 import java.io.*;


 import javax.swing.*;


 public class NodePade extends JFrame{

 	private JTextField selectField;
	private JTextArea editArea;
	private JButton saveBtn;
	private JButton openFileBtn;
	
	private int level = 0;
	
	public NodePade(){
		this.init();
	}
	
	private void init(){
		this.setTitle("简单的笔记本");
		this.setBounds(300,50,600,650);
		selectField = new JTextField(40);
		openFileBtn = new JButton("浏览");
		openFileBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				NodePade.this.level = 0;
				String path = selectField.getText();
				openDirOrFile(path.replaceAll("/", "\\"));
			}
		});
		JPanel upPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
		upPanel.setBackground(Color.CYAN);
		upPanel.add(selectField);
		upPanel.add(openFileBtn);
		this.add(upPanel,BorderLayout.NORTH);
		/*
		 * 文本编辑区
		 */
		editArea = new JTextArea();
		ScrollPane scollPanel = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scollPanel.add(editArea);
		this.add(scollPanel, BorderLayout.CENTER);
		
		/*
		 * 保存按钮
		 */
		saveBtn = new JButton("保存");
		saveBtn.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ae){
				saveFile();
			}
		});
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.green);
		southPanel.add(saveBtn);
		this.add(southPanel, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	/*
	 * 保存文件
	 */
	private void saveFile(){
		FileDialog fd = new FileDialog(this,"保存文件");
		fd.setFile("*.java");
		//设置为保存模式
		fd.setMode(FileDialog.SAVE);
		fd.setVisible(true);
		//获取文件名
		String fileName = fd.getFile();
		String dir = fd.getDirectory();
		File newFile = new File(dir +File.separator+fileName);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
				newFile)));
			String str = editArea.getText();
			pw.println(str);
			pw.flush();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			pw.close();
		}
	}
	/**
	 * 打开目录或文件
	 * 
	 * 指定目录或文件或文件的绝对路径名
	 */
	private void openDirOrFile(String absolutePath){
		File file = new File(absolutePath);  
		
		if (!(file.exists())){
			editArea.setText("文件不存在");
		}else if (file.isDirectory()){
			editArea.setText(null);
			showDir(file);
		}else if (file.isFile()){
			try{
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				String str = null;
				editArea.setText(null);
				while((str = br.readLine())!=null){
					editArea.append(str+"\r\n");
				}
				br.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	private void showDir(File directory){
		File[] files = directory.listFiles();
		int len = files.length;
		for (int i = 0; i < len; i++){
			if (files[i].isDirectory()){
				for (int j = 0; j < this.level; j++){
					editArea.append("  ");
				}
				editArea.append(this.level+1+"  +"+files[i].getName()+" 文件夹\r\n");
				this.level++;
				showDir(files[i]);
			}else if (files[i].isFile()){
				for (int j = 0; j < this.level + 2; j++){
					editArea.append(" ");
				}
				editArea.append(this.level+"|----"+files[i].getAbsolutePath()+"\r\n");
				
			}
		}
	}
	/*
	 * 测试
	 */
	public static void main(String[] args){
		new NodePade();
	}
}

