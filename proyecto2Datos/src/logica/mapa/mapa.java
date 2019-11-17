/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.mapa;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import logica.graphs.Edge;
import logica.graphs.Floyd.AlgoritmoDijkstra;
import logica.graphs.Floyd.coleccionCamino;
import logica.graphs.GVertex;
import logica.graphs.Graph;
import logica.repartidor.coleccionRepartidor;
import logica.repartidor.crearRepartidor;
import logica.repartidor.repartidor;
import logica.serializacion.serializarXML;

/**
 *
 * @author Bryan
 * @param <V>
 * @param <E>
 */
@XmlRootElement(name = "mapa")
//@XmlType(propOrder = {"ima","Graph"})
public class mapa<V, E> {

    private Graph<V, E> grafo;
    private coleccionCamino<V, E> caminosPosibles;
    private coleccionRepartidor<V, E> colRepartidor;
//    private boolean active = false;
    private String ubicacionImagen;

    public mapa() {
        this(new Graph<>());
    }

    public mapa(Graph<V, E> grafo) {
        this.grafo = grafo;
        aumentarPesos();
        this.caminosPosibles = new coleccionCamino();
        colRepartidor = new coleccionRepartidor();
        ubicacionImagen = "";

    }

    public void setGrafo(Graph<V, E> grafo) {
        this.grafo = grafo;
    }

    public void setCaminosPosibles(coleccionCamino<V, E> caminosPosibles) {
        this.caminosPosibles = caminosPosibles;
    }

    public void setColRepartidor(coleccionRepartidor<V, E> colRepartidor) {
        this.colRepartidor = colRepartidor;
    }

    public void setUbicacionImagen(String ubicacionImagen) {
        this.ubicacionImagen = ubicacionImagen;
    }

    public Edge<V, E> getEdge(V v1, V v2) {
        return grafo.getEdge(v1, v2);
    }

    @XmlElement(name = "ima")
    public String getUbicacionImagen() {
        return ubicacionImagen;
    }

    @XmlElement(name = "Graph")
    public Graph<V, E> getGrafo() {
        return grafo;
    }

    public coleccionCamino<V, E> getCaminosPosibles() {
        return caminosPosibles;
    }

    public coleccionRepartidor<V, E> getColRepartidor() {
        return colRepartidor;
    }

    public void add(V v, Point2D.Float position) {
        grafo.add(v, position);
    }

    public void add(V v) {
        grafo.add(v);
    }

    public void add(V t, V h, E w) {
        grafo.add(t, h, w);
    }

    public void add(V t, V h) {
        grafo.add(t, h);
    }

    public void add(repartidor repartidor) {
        if (repartidor != null) {
            System.out.println(repartidor);
            colRepartidor.add(repartidor);
            repartidor.init();
            repartidor.aumentarPesos(grafo);
            this.calcularRutasMinimas();
        }

    }

    public void add(String id, V inicio, V destino) {
        repartidor<V, E> ptr = new repartidor<>(id, this.caminosPosibles.buscarRuta(inicio, destino));
        System.out.println(ptr);
        this.colRepartidor.add(ptr);
        ptr.init();
        ptr.aumentarPesos(grafo);
        this.calcularRutasMinimas();
    }

    private void aumentarPesos() {

        for (Iterator<Edge<V, E>> it = this.grafo.getEdges().iterator(); it.hasNext();) {
            Edge e = it.next();
            double peso = new Random().nextInt(74) + 10;
            peso += new Random().nextDouble();
            String num = String.format("%.2f", peso);
            e.setInfo((num));
        }

    }

    private void crearRepartidores() {
        crearRepartidor.crearRepartidores(caminosPosibles, this);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Lista Vertices:%n%s%n", grafo.getVertex()));
        s.append(String.format("Lista Aristas:%n%s%n", grafo.getEdges()));
        s.append(String.format("Caminos minimos de cada vertice:%n%s%n", caminosPosibles));
        s.append(String.format("Repartidores:%n%s%n", colRepartidor));
        return s.toString();
    }

    private void calcularRutasMinimas() {
//        this.caminosPosibles.limpiarLista();
        AlgoritmoDijkstra<V, E> floyd = new AlgoritmoDijkstra();
        this.caminosPosibles = floyd.algoritmoDijkstra(grafo);
    }

//    public boolean isActive() {
//        return active;
//    }
//    public void setActive(boolean active) {
//        this.active = active;
//    }
    public void init() {
        calcularRutasMinimas();
        crearRepartidores();
        colRepartidor.init();
    }

//    public void init(GVertex<V> pathStart) {
//
//        new Thread() {
//            @Override
//            public void run() {
//                GVertex<V> v0 = pathStart;
//                List<GVertex<V>> vs = colRepartidor.getColeccionRepartidor().
//                getLast().getCaminoRepartidor().getRuta();
//                
//                Iterator<GVertex<V>> i=vs.getIterator();
//                while (i.hasNext()) {
//                    
//                    p0 = v0.getPosition();
//
//                    // Se define el criterio para seleccionar
//                    // el siguiente vértice.
//                    GVertex<V> v1 = i.getNext();
//                    p1 = v1.getPosition();
//
//                    System.out.printf("v(%s): %s%n", v0.getInfo(), p0);
//                    System.out.printf("v(%s): %s%n", v1.getInfo(), p1);
//                    t = 0.0;
//                    while (t <= 1.0) {
//                        t += DT;
//                        try {
//                            Thread.sleep(MAX_WAIT);
//                        } catch (InterruptedException ex) {
//                        }
//                    }
//                    v0 = v1;
//                }
//            }
//
//        }.start();
//
//
//    }
    public void paint(Graphics bg, Rectangle bounds) {
        Graphics2D g = (Graphics2D) bg;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.DARK_GRAY);
        g.setStroke(TRAZO_GUIA);
//        Rectangle b = getBounds();
//        g.drawRect(b.x, b.y, b.width, b.height);
//
        g.setFont(TIPO_BASE);
        FontMetrics fm = g.getFontMetrics();
//        Iterator<Edge<V, E>> i = this.grafo.getEdges().getIterator();
//        while (i.hasNext()) {
//            Edge<V, E> e = i.getNext();

//        /*dibuja el trazo que une cada vertice*/
//            g.setStroke(TRAZO_BASE);
//            g.setColor(Color.WHITE);
//            g.drawLine(
//                    (int) e.getTail().getPosition().x,
//                    (int) e.getTail().getPosition().y,
//                    (int) e.getHead().getPosition().x,
//                    (int) e.getHead().getPosition().y
//            );

        /*Dibuja una linea al centro del trazo que une cada vertice*/
//            g.setStroke(new BasicStroke(1f));
//            g.setColor(Color.BLACK);
//            g.drawLine(
//                    (int) e.getTail().getPosition().x,
//                    (int) e.getTail().getPosition().y,
//                    (int) e.getHead().getPosition().x,
//                    (int) e.getHead().getPosition().y
//            );
//        }
        g.setStroke(TRAZO_VERTICE);
//        Iterator<GVertex<V>> j = grafo.getVertex().getIterator();
//        while (j.hasNext()) {
        for (GVertex<V> v : grafo.getVertex()) {
//            GVertex<V> v = j.getNext();

//            g.setColor(Color.GRAY);
//            g.fillOval((int) v.getPosition().x - diamentroVertice / 2 + 4,
//                    (int) v.getPosition().y - diamentroVertice / 2 + 4,
//                    diamentroVertice, diamentroVertice);
//            g.setColor(Color.WHITE);
//            g.fillOval((int) v.getPosition().x - diamentroVertice / 2,
//                    (int) v.getPosition().y - diamentroVertice / 2,
//                    diamentroVertice, diamentroVertice);
            String tt = String.format("%s", v.getInfo());
            g.setColor(Color.BLACK);
            g.drawString(tt,
                    v.getPosition().x - fm.stringWidth(tt) / 2,
                    v.getPosition().y + fm.getAscent() / 2);
        }
//        }
        colRepartidor.paint(bg, bounds);
    }

    public void guardarMapa(String s) {
        serializarXML guardar = new serializarXML();
        guardar.guardar(this, s);
    }

    public static mapa cargarMapa(String s) throws FileNotFoundException, JAXBException {
        String archivoPrueba = "./" + s + ".xml";

        InputStream entrada = new FileInputStream(archivoPrueba);
        JAXBContext context = JAXBContext.newInstance(mapa.class);
        Unmarshaller mar = context.createUnmarshaller();
        mapa map = (mapa) mar.unmarshal(entrada);

        return map;

    }
    private static final float[] DASHES = {4f, 4f};
//    private static final Stroke TRAZO_MARCADOR = new BasicStroke(8f);
//    private Point2D.Float p0;
//    private Point2D.Float p1;
//    private final double t = 0.0;
//    private static final int S1 = 56;
    private static final Stroke TRAZO_VERTICE = new BasicStroke(2f);
    private static final Stroke TRAZO_BASE
            = new BasicStroke(12f,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, null, 0f);
    private static final Stroke TRAZO_GUIA
            = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0f, DASHES, 0f);
    private static final Font TIPO_BASE
            = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    private static final int diamentroVertice = 25;
    private static final int S0 = 25;
//    private static final int MAX_WAIT = 25;
//    private static final double DT = 0.035;
}
