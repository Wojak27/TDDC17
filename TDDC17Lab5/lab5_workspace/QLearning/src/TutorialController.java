import java.util.ArrayList;
import java.util.List;

public class TutorialController extends Controller {

    public SpringObject object;

    ComposedSpringObject cso;

    /* These are the agents senses (inputs) */
	DoubleFeature x; /* Positions */
	DoubleFeature y;
	DoubleFeature vx; /* Velocities */
	DoubleFeature vy;
	DoubleFeature angle; /* Angle */
	String[] nameList = {"x","y", "vx", "vy", "angle"};
	DoubleFeature[] featureList = new DoubleFeature[5];
	

    /* Example:
     * x.getValue() returns the vertical position of the rocket 
     */

	/* These are the agents actuators (outputs)*/
	private RocketEngine leftRocket;
	private RocketEngine middleRocket;
	private RocketEngine rightRocket;

    /* Example:
     * leftRocket.setBursting(true) turns on the left rocket 
     */
	
	public void init() {
		System.out.println("hello");
		cso = (ComposedSpringObject) object;
		featureList[0] = (DoubleFeature) cso.getObjectById("x");
		featureList[1] = (DoubleFeature) cso.getObjectById("y");
		featureList[2] = (DoubleFeature) cso.getObjectById("vx");
		featureList[3] = (DoubleFeature) cso.getObjectById("vy");
		featureList[4] = (DoubleFeature) cso.getObjectById("angle");

		leftRocket = (RocketEngine) cso.getObjectById("rocket_engine_left");
		rightRocket = (RocketEngine) cso.getObjectById("rocket_engine_right");
		middleRocket = (RocketEngine) cso.getObjectById("rocket_engine_middle");
		
	}
	public void tick(int currentTime){
		init();
		
		int i = 0;
		for(DoubleFeature feature : featureList){
			System.out.println(feature);
			System.out.println(featureList.length);
			if(feature != null){
				System.out.println(nameList[i]+": "+ feature.getValue());
				i++;
			}
		}
		
	}

}
