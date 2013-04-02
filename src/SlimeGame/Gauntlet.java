package SlimeGame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import neuralnetwork.neuroevolution.OvernightAI;
import neuralnetwork.neuroevolution.SlimeAgent;
import neuralnetwork.neuroevolution.bestai.BeatsDefenseAgent;
import neuralnetwork.neuroevolution.bestai.DefenseAgent;
import neuralnetwork.neuroevolution.bestai.TooSickAI;
import SlimeGame.SlimeV2.ServeSide;

public class Gauntlet implements Callable<GameResult>{


	ServeSide side;
	SlimeAI challenger;
	SlimeAI evaluatedAI;
	int STARTING_POINTS;
	public Gauntlet(ServeSide side, SlimeAI challenger,
			SlimeAI evaluatedAI, int sTARTING_POINTS) {
		super();
		this.side = side;
		this.challenger = challenger;
		this.evaluatedAI = evaluatedAI;
		STARTING_POINTS = sTARTING_POINTS;
	}


	@Override
	public GameResult call() throws Exception {
		return SlimeV2.determineVictor(false, side, challenger, evaluatedAI, STARTING_POINTS);
	}
		
	
	
    public static void theGauntlet(SlimeAI evaluatedAI) throws InterruptedException, ExecutionException {
       
        List<SlimeAI> challengers = new LinkedList<SlimeAI>();
        challengers.add(new CrapSlimeAI());
        challengers.add(new DannoAI());
        challengers.add(new DannoAI2());
        challengers.add(new ThreeSwapSlimeAI());
        challengers.add(new TooSickAI());
        challengers.add(new DefenseAgent());
        challengers.add(new BeatsDefenseAgent());
        int[] wins = new int[challengers.size()];
        int[] losses = new int[challengers.size()];
        int[] ties = new int[challengers.size()];

        final int NTHREADS = Runtime.getRuntime().availableProcessors();
        
        // threading is broken,  dunn why
        ExecutorService service = Executors.newFixedThreadPool(1);
        ArrayList<Future<GameResult>> futureResult = new ArrayList<Future<GameResult>>();
        
        
        int NUM_GAMES = 50;
        int STARTING_POINTS = 5;
        ServeSide side;
        int totalWins = 0;
        int totalLosses = 0;
        int totalTies = 0;

        for (int i = 0; i < challengers.size(); i++) {
            SlimeAI challenger = challengers.get(i);
            for (int j = 0; j < NUM_GAMES; j++) {
                if (j % 2 == 0) {
                    side = SlimeV2.ServeSide.RIGHT;
                } else {
                    side = SlimeV2.ServeSide.LEFT;
                }
                Gauntlet futureMatch = new Gauntlet(side, challenger, evaluatedAI, STARTING_POINTS);
                Future<GameResult> result = service.submit(futureMatch);
                
                
                //result.get();
                
                futureResult.add(result);
            }
        }

        System.out.println("==================");
        int cur = 0;
        for (int i = 0; i < challengers.size(); i++) {
        	for (int j = 0; j < NUM_GAMES; j++) {
        		GameResult result = futureResult.get(cur++).get();
        		if (result.getWinner() == 1) {
                    wins[i] = wins[i] + 1;
                    totalWins++;
                } else if (result.getWinner() == 0) {
                    losses[i] = losses[i] + 1;
                    totalLosses++;
                } else {
                    ties[i] = ties[i] + 1;
                    totalTies++;
                }
        	}
            SlimeAI challenger = challengers.get(i);
            System.out.println(challenger.getClass().getSimpleName() + " = "
                    + wins[i] + "/" + NUM_GAMES + "W | "
                    + losses[i] + "/" + NUM_GAMES + "L | "
                    + ties[i]  + "/" + NUM_GAMES + "T"
            );
        }

        System.out.println("Total Wins   = " + totalWins + "/" + challengers.size()*NUM_GAMES);
        System.out.println("Total Losses = " + totalLosses + "/" + challengers.size()*NUM_GAMES);
        System.out.println("Total Ties   = " + totalTies + "/" + challengers.size()*NUM_GAMES);
        System.out.println("==================");
        
        service.shutdown();
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
    	OvernightAI ai = new OvernightAI();
    
		theGauntlet( ai);
	}
}
