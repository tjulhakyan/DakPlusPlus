package dpp.aplication.model;

public class InCome {
	private int projectId;
	private double incomePerProject;
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public double getIncomePerProject() {
		return incomePerProject;
	}
	public void setIncomePerProject(double incomePerProject) {
		this.incomePerProject = incomePerProject;
	}
	@Override
	public String toString() {
		return "InCome [projectId=" + projectId + ", incomePerProject=" + incomePerProject + "]";
	}
	
}
