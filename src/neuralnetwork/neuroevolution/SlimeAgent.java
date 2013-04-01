package neuralnetwork.neuroevolution;

import SlimeGame.Constants;
import SlimeGame.CrapSlimeAI;
import SlimeGame.SlimeAI;
import SlimeGame.SlimeV2;

import java.util.Random;

public class SlimeAgent extends SlimeAI implements Agent {
	
	public neuralnetwork.core.NeuralNetwork nn;
	//NeuralNetworkSlimeAi ai;
	
	public static final int NUM_HIDDEN_NODES = 25;
	/** Outputs passed back to the input at each day */
	public static final int NUM_MEMORY_NODES = 0;

    public static final int NUM_INPUT_NODES = 12;
    public static final int NUM_OUTPUT_NODES = 2;

	
	private static Random r = new Random();
	
	
	public SlimeAgent() {
		nn = new neuralnetwork.core.NeuralNetwork(NUM_INPUT_NODES + NUM_MEMORY_NODES, NUM_HIDDEN_NODES, NUM_OUTPUT_NODES + NUM_MEMORY_NODES);
		nn.randomizeWeights();
//        double[] weights = {-0.6718790531158447, 2.232470989227295, -0.78806907748208, -0.7394404411315918, -0.5557193756103516, 0.3811624050140381, 2.5071287155151367, -1.9758613109588623, -2.558081328868866, 2.462517738342285, 2.618867874145508, 0.2670454978942871, 1.599388599395752, -1.0908405780792236, -3.1861793994903564, 0.4330143928527832, 0.13303367517765152, 1.9465460777282715, -0.7235774993896484, 2.4349708557128906, -3.403820812702179, -2.253826141357422, -0.2106313705444336, -0.7901954650878906, 0.6786270141601562, 1.7407054901123047, 2.7301440238952637, -0.5940861701965332, -3.2465888261795044, -2.6052690744400024, -0.3884258270263672, -0.5262703895568848, 2.587728500366211, 0.7732009887695312, 1.88071870803833, 0.10820960998535156, 0.9087624549865723, -0.08329582214355469, -3.4934794902801514, 0.941187858581543, 2.9271178245544434, -2.6161258816719055, 0.4896419048309326, 1.398214340209961, -2.9630947709083557, 1.1880865097045898, -1.5445972681045532, -2.176589012145996, 3.3033084869384766, 2.6468114852905273, -0.39082884788513184, -1.3803374767303467, 0.7497334480285645, -1.381422519683838, -1.010751724243164, 2.4943642616271973, 0.16136717796325684, -0.7616329991748898, 2.5467424392700195, -2.448708415031433, -3.289743185043335, 2.800119400024414, 1.2816572189331055, 2.272158145904541, -0.6478161811828613, 0.8857903480529785, -0.7749743461608887, -0.3931009769439697, -1.0886540412902832, -2.855989933013916, 2.1784238815307617, -1.5058033466339111, -0.4679722785949707, 0.7995534900235768, -2.021043062210083, 2.7800393104553223, 0.22888249749020795, 2.968648910522461, 3.1785178184509277, 1.9333491325378418, -3.2107810974121094, 0.24092721939086914, 3.3033337593078613, 0.2833731174468994, 0.6411886215209961, 1.7922625541687012, -1.7646398544311523, -1.2204463481903076, -2.037293791770935, -2.7310571670532227, -1.5981385707855225, 3.423567771911621, 0.9450597699749159, -0.3978009966273095, -3.4477387070655823, 2.1146836280822754, -2.5347772240638733, -1.4560456275939941, 1.5624127388000488, 2.542733669281006, 2.6423511505126953, -3.1264041662216187, -2.4948025941848755, -0.5270748138427734, -2.861696422100067, -2.5848960280418396, 1.6205716133117676, 2.94149112701416, -0.504206657409668, -1.2889823913574219, 2.530705451965332, 3.270388126373291, 0.3789539337158203, 0.9170417785644531, 3.128702163696289, -3.0495816469192505, 2.378063201904297, -1.9853713512420654, 1.3419890403747559, -2.4798556566238403, 1.3158822059631348, 3.039560317993164, 0.009592056274414062, -0.3935561180114746, 2.3654279708862305, -1.4884982109069824, 3.4819865226745605, 1.284438133239746, 3.032195568084717, 2.791383743286133, 0.4695267677307129, -2.5092246532440186, 2.371699810028076, -0.2081000804901123, 3.056453227996826, -2.4575023651123047, 0.7645411491394043, -0.30472230911254883, -0.6842942237854004, -2.7396721839904785, 0.35517072677612305, -3.2291476726531982, -1.1886396408081055, 2.4617390632629395, -2.1478466987609863, 0.25426530838012695, -1.8101036548614502, 3.256281852722168, -1.6118531227111816, 0.3021514415740967, -2.1083505153656006, -3.1447598934173584, 1.374647617340088, -3.4004520773887634, -0.37273776716259155, 0.20166137793809646, -1.0587635040283203, -1.4502735137939453, 2.62123966217041, -0.6295504570007324, 3.398498058319092, 1.4122905731201172, -3.059526801109314, 0.9791226387023926, 0.19718456268310547, -3.0756056904792786, 1.2085685729980469, -0.9569254462895147, 3.20809268951416, 2.761439800262451, 0.0358431339263916, 2.281095504760742, -3.0797250270843506, 0.07230122157052343, -3.2477357983589172, 0.22366278781010718, -1.9078702926635742, 0.5093898773193359, -0.5719080579677276, -2.79386442899704, 0.4829111099243164, -1.471647024154663, -3.0916925072669983, 0.4988577365875244, 1.1716375350952148, 1.2696881294250488, 2.6190404891967773, -3.448458433151245, -3.439971923828125, -2.1110050678253174, -1.3042709827423096, -2.3835291862487793, -2.739008367061615, -2.306884765625, 0.3170470416111175, 3.022507667541504, -3.312503218650818, 1.6994924545288086, 0.4641788763706056, -1.743492841720581, 3.0988783836364746, -2.6334643959999084, 2.6825504302978516, -1.052555799484253, -0.4046821594238281, 2.2929916381835938, 1.9050946235656738, 0.24703669548034668, 2.802006721496582, 1.6351652145385742, 0.6571474075317383, -2.1931793689727783, 1.5510172843933105, -1.9484508037567139, 0.28929734230041504, -0.7563457489013672, -3.05447119474411, -2.8948034048080444, -3.2686671018600464, 0.30518603324890137, 1.499974250793457, -1.5094866752624512, -1.028980016708374, -3.0375620126724243, -1.2714276313781738, 0.8013863563537598, 1.8249688148498535, 0.6301665306091309, -1.4669170379638672, -2.5646990537643433, 1.5555758476257324, 0.7557716369628906, -0.9400081634521484, -0.7593874931335449, 1.2745933532714844, -2.286383628845215, 2.4257278442382812, -1.6757164001464844, -1.0433204174041748, 1.7383723258972168, -0.556208610534668, 2.529381275177002, -3.2665780186653137, -0.805060863494873, 3.412705421447754, 0.48786783399570166, -0.3796577453613281, 0.33502721786499023, 0.5075317100239625, -1.278554916381836, 2.4174108505249023, 1.6582107543945312, -2.62248033285141, -3.3063782453536987, -0.7406861550935109, 2.7885327339172363, 2.0221123695373535, 0.02643132209777832, -3.455876410007477, -0.03950929641723633, -0.5117528438568115, 1.8813896179199219, 1.7666616439819336, 3.1484785079956055, 0.8547361581446784, 2.8791322708129883, 2.376466751098633, -0.2578077181649825, -2.7631569504737854, 2.892735481262207, 2.833526611328125, 2.791924476623535, -2.2630032300949097, -0.1439685821533203, 0.2795580759261773, 1.846855640411377, -1.2214069366455078, -2.4675848484039307, 2.414586067199707, -3.0945213437080383, -1.1156105995178223, 0.497904863203503, -1.1119790077209473, 0.469708517682381, -2.6412224173545837, 0.2392716407775879, -1.4711217880249023, -0.2636721134185791, 0.2634040396160868, -1.6405198574066162, -3.1193212270736694, -1.057868480682373, -1.9429137706756592, -1.8153631687164307, -0.17496943473815918, -0.8563113212585449, -1.6030890941619873, 0.4942803382873535, 0.30434274673461914, -2.3229753971099854, 2.9808154106140137, -1.9957048892974854, -2.1806511878967285, 0.6137075424194336, 0.06718063354492188, -1.2449207305908203, 0.011960744857788086, -0.24925756454467773, -1.5730211734771729, 2.857283115386963, -1.8007960319519043, 0.3948912095113559, 2.9982495307922363, 0.8130807876586914, 1.1313886642456055, 3.1752753257751465, 0.8695258456322759, -1.0691308975219727, -3.1244060397148132, 0.7001919746398926, 3.302224636077881, 0.39214301109313965, 3.2318639755249023, 0.9935564994812012, 1.7086191177368164, 2.3712282180786133, 0.8723555518854444, 1.6056289672851562, 1.6056394577026367, 3.468989372253418, -1.1305394172668457, -3.3447523713111877, -1.9947185516357422, -3.1724653840065002, -2.278336524963379, -1.9072407484054565, 2.186328887939453, 2.0560154914855957, 1.959822654724121, -1.333040714263916, 2.8734331130981445, -1.9580483436584473, 3.1678519248962402, 2.858168601989746, 1.6713776588439941, 3.4674158096313477, 1.1360702514648438, -1.570500373840332, 2.8583874702453613, 0.4778125286102295, -0.958357572555542, 0.24889302253723145};
//        nn.loadWeights(weights);
	}
	
	public double evaluateFitness() {
//
//		// play 10 games

        int NUM_GAMES = 10;

        int wins = 0;
        for (int i = 0; i < NUM_GAMES; i++) {
            SlimeAI ai = new CrapSlimeAI();
            int winner = SlimeV2.determineVictor(false, SlimeV2.ServeSide.LEFT, ai, this);
            if (winner == 1) {
                wins++;
            }
        }

        return wins;
	}

	@Override
	public Agent mutate(double mutationRate) {
		double[] weights = nn.retrieveWeights();

		// mutate some of these weights
		for (int i = 0; i < weights.length; i++) {
			if (r.nextFloat() < mutationRate) {
				weights[i] = r.nextFloat() * 7 - 3.5;
			}
		}

		SlimeAgent a = new SlimeAgent();
		a.nn.loadWeights(weights);
		
		return a;
	}

    @Override
    public void save() {
        // just printing for now
        double[] weights = nn.retrieveWeights();
        System.out.print("{");
        for (double weight : weights) {
            System.out.print(weight + ",");
        }
        System.out.print("}");
    }
    
    double normalizeVelocity(double velocity)
    {
    	// what is the maximum velocity anyways?
    	final double maxVelocity = 100;
    	
    	// normalize (-maxVelocity,maxVelocity) to (-1,1)
    	return (velocity ) / maxVelocity;
    }


    @Override
    public void moveSlime() {
    	double gameWidth = Constants.GAME_WIDTH;
    	double gameHeight = Constants.GAME_HEIGHT;
    	
    	// normalize these inputs
        double[] inputs = new double[]{
        		ballX / gameWidth, 
        		ballY / gameHeight, 
        		normalizeVelocity(ballVX), 
        		normalizeVelocity(ballVY), 
        		p1X / gameWidth, 
        		p1Y / gameHeight, 
        		normalizeVelocity(p1XV), 
        		normalizeVelocity(p1YV), 
        		p2X / gameWidth, 
        		p2Y / gameHeight, 
        		normalizeVelocity(p2XV),
        		normalizeVelocity(p2YV)};
        double[] outputs = nn.process(inputs);

        if (outputs[0] < 0.4) {
            startMoveTowardsNet();
        } else if (outputs[0] > 0.6) {
            startMoveAwayFromNet();
        } else {
            stopMoving();
        }

        if (outputs[1] > 0.5) {
            startJump();
        }
    }

    public static void main(String[] args) {
        SlimeGame.SlimeV2.determineVictor(true, SlimeV2.ServeSide.LEFT, new CrapSlimeAI(), new SlimeAgent());
    }
}
