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
	DoubleFeature[] featureList = {x,y,vx, vy, angle};
	

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
		x = (DoubleFeature) cso.getObjectById("x");
		y = (DoubleFeature) cso.getObjectById("y");
		vx = (DoubleFeature) cso.getObjectById("vx");
		vy = (DoubleFeature) cso.getObjectById("vy");
		angle = (DoubleFeature) cso.getObjectById("angle");

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
