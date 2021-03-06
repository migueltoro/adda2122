package us.lsi.alg.bufete;


import java.util.Locale;
import java.util.function.Predicate;

import org.jgrapht.GraphPath;

import us.lsi.graphs.Graphs2;
import us.lsi.graphs.alg.DPR;
import us.lsi.graphs.alg.DynamicProgramming.PDType;
import us.lsi.graphs.alg.DynamicProgrammingReduction;
import us.lsi.graphs.alg.GraphAlg;
import us.lsi.graphs.alg.GreedySearchOnGraph;
import us.lsi.graphs.virtual.EGraph;

public class TestPD {

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));

		for (Integer id_fichero = 1; id_fichero < 4; id_fichero++) {

			DatosBufete.iniDatos("ficheros/bufete" + id_fichero + ".txt");

			System.out.println("\n\n>\tResultados para el test " + id_fichero + "\n");
			final BufeteVertex start = BufeteVertex.initialVertex();
			Predicate<BufeteVertex> goal = BufeteVertex.goal();

			/**
			 * IMPORTANTE. En este tipo se usa el tipo "Last".
			 */
			EGraph<BufeteVertex, BufeteEdge> graph = 
					Graphs2.simpleVirtualGraphLast(start,goal, null,v->true,v -> (double) v.maxCarga());

			System.out.println("\n\n#### Algoritmo PD ####");
			
			GreedySearchOnGraph<BufeteVertex, BufeteEdge> rr = 
					GraphAlg.greedy(graph,
							BufeteVertex::greadyEdge);
			
			GraphPath<BufeteVertex, BufeteEdge> path = rr.search().orElse(null);
			SolucionBufete sm = SolucionBufete.of(path);
			Double bv = path.getWeight();
			System.out.println(bv);
			
			// Algoritmo PD
			DynamicProgrammingReduction<BufeteVertex, BufeteEdge> pdr = 
					DPR.dynamicProgrammingReduction(graph,
					Heuristica::heuristic, 
					PDType.Min);
			
			pdr.bestValue = bv;

			GraphPath<BufeteVertex, BufeteEdge> gp_pdr = pdr.search().orElse(null); // getEdgeList();
			if ( gp_pdr != null) {
				SolucionBufete s_pdr = SolucionBufete.of(gp_pdr);
				System.out.println(s_pdr.maxCarga());
				System.out.println(s_pdr);
			} else {
				System.out.println(sm.maxCarga());
				System.out.println(sm);
			}
		}
	}
}

