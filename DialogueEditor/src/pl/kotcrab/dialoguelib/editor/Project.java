package pl.kotcrab.dialoguelib.editor;

public class Project
{

	private String projectName;
	private String projectMainDir;
	
	private String projectOut;
	
	private boolean gzipProject;
	private boolean gzipExport;
	
	public Project(String projectName, String projectMainDir, boolean gzipProject, boolean gzipExport)
	{
		this.projectName = projectName;
		this.projectMainDir = projectMainDir;
		
		this.gzipProject = gzipProject;
		this.gzipExport = gzipExport;
	}
	
	public void setCustomOut(String projectOut)
	{
		this.projectOut = projectOut;
	}
	
}
