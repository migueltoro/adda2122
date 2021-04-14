package us.lsi.alg.typ.manual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class TyPBT {
	
	
	
	public static record StateTyP(TyPProblem vertice, Integer valorAcumulado, List<Integer> acciones, List<TyPProblem> vertices) {
		
		public static StateTyP of(TyPProblem vertex, Integer valorAcumulado, List<Integer> acciones, List<TyPProblem> vertices) {
			return new StateTyP(vertex, valorAcumulado, acciones,vertices);
		}
		
		public static StateTyP of(TyPProblem vertex) {
			List<TyPProblem> vt = List.of(vertex);
			return new StateTyP(vertex,0,new ArrayList<>(),vt);
		}

		StateTyP forward(Integer a) {
			List<Integer> as = new ArrayList<>(this.acciones());
			as.add(a);
			List<TyPProblem> vt = new ArrayList<>(this.vertices());
			TyPProblem vcn = this.vertice().vecino(a);
			vt.add(vcn);
			return StateTyP.of(vcn,vcn.maxCarga(),as,vt);
		}

		StateTyP back(Integer a) {
			List<Integer> as = new ArrayList<>(this.acciones());
			as.remove(as.size()-1);
			List<TyPProblem> vt = new ArrayList<>(this.vertices());
			vt.remove(vt.size()-1);
			TyPProblem van = vt.get(vt.size()-1);
			return StateTyP.of(van,van.maxCarga(),as,vt);
		}
		
		SolucionTyP solucion() {
			return SolucionTyP.of(start,this.acciones());
		}
	}
	
	public static TyPProblem start;
	public static StateTyP estado;
	public static Integer minValue;
	public static Set<SolucionTyP> soluciones;
	
	public static void btm(Integer minValue) {
		TyPBT.start = TyPProblem.first();
		TyPBT.estado = StateTyP.of(start);
		TyPBT.minValue = minValue;
		TyPBT.soluciones = new HashSet<>();
		btm();
	}
	
	public static void btm(Integer minValue, SolucionTyP s) {
		TyPBT.start = TyPProblem.first();
		TyPBT.estado = StateTyP.of(start);
		TyPBT.minValue = minValue;
		TyPBT.soluciones = new HashSet<>();
		TyPBT.soluciones.add(s);
		btm();
	}
	
	public static void btm() {
		if(TyPBT.estado.vertice().index() == DatosTyP.n) {
			Integer value = estado.valorAcumulado();
			if(value < TyPBT.minValue) {
				TyPBT.minValue = value;
				TyPBT.soluciones.add(TyPBT.estado.solucion());
			}
		} else {
			List<Integer> alternativas = TyPBT.estado.vertice().acciones();
			for(Integer a:alternativas) {	
				Integer cota = Heuristica.cota(TyPBT.estado.vertice(),a);
				if(cota >= TyPBT.minValue) continue;
				TyPBT.estado = TyPBT.estado.forward(a);
				btm();  
				TyPBT.estado = TyPBT.estado.back(a);
			}
		}
	}

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));
		DatosTyP.datos("ficheros/tareas.txt",5);
		TyPProblem v1 = TyPProblem.first();
		SolucionTyP s = Heuristica.solucionVoraz(v1);
		System.out.println(s);
		long startTime = System.nanoTime();
		TyPBT.btm(Integer.MAX_VALUE);
		long endTime = System.nanoTime() - startTime;
		System.out.println("1 = "+endTime);
		System.out.println(TyPBT.soluciones.stream().min(Comparator.comparing(x->x.maxCarga())).get());
		startTime = System.nanoTime();
		TyPBT.btm(s.maxCarga(),s);
		long endTime2 = System.nanoTime() - startTime;
		System.out.println("2 = "+1.*endTime2/endTime);
		System.out.println(TyPBT.soluciones.stream().min(Comparator.comparing(x->x.maxCarga())).get());
	}


}
