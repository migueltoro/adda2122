package us.lsi.alg.productos;


import us.lsi.graphs.virtual.ActionSimpleEdge;

public record ProductosEdge(ProductosVertex source, ProductosVertex target, Integer action, Double weight)
           implements ActionSimpleEdge<ProductosVertex, Integer> {
	
	public static ProductosEdge of(ProductosVertex origen, ProductosVertex destino, Integer action) {
		Double coste = DatosProductos.getProducto(origen.indice).precio()*action;
		return new ProductosEdge(origen, destino, action, coste);
	}

}
