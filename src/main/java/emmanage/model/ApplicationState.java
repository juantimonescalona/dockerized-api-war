package emmanage.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Represents application state.
 * @author zavora
 */
public class ApplicationState{
	public static final ApplicationState DEPLOYED = new ApplicationState("DEPLOYED");
	public static final ApplicationState UNDEPLOYED = new ApplicationState("UNDEPLOYED");
	public static final ApplicationState ISDEPLOYING = new ApplicationState("ISDEPLOYING");
	public static final ApplicationState ISUNDEPLOYING = new ApplicationState("ISUNDEPLOYING");

	private String state;
	private transient boolean mutable = true;
	
	public ApplicationState(){}
	public ApplicationState(String state){setState(state); mutable = true;}

	@ApiModelProperty(value = "papplication status", allowableValues = "DEPLOYED,UNDEPLOYED,ISDEPLOYING,ISUNDEPLOYING")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		if (mutable) this.state = state;
	}
}
