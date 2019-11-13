package logica.repartidor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import lists.Iterator;
import lists.List;
import logica.graphs.Floyd.camino;
import logica.graphs.GVertex;
import logica.graphs.Graph;

public class repartidor<V, E> {

    private String identificador;
    private camino<V, E> caminoRepartidor;
    private String ubicacionParcialImagen;
    private final int cantidadImagen = 4;

    public repartidor(String identificador, camino<V, E> caminoRepartidor) {
        this.identificador = identificador;
        this.caminoRepartidor = caminoRepartidor;
        Random r = new Random();
        int valorDado = r.nextInt(cantidadImagen) + 1;
        ubicacionParcialImagen = "/ima/imaRepartidor/repartidor" + valorDado + ".png";
    }

    public repartidor() {
        this("", new camino());
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public camino<V, E> getCaminoRepartidor() {
        return caminoRepartidor;
    }

    public void setCaminoRepartidor(camino<V, E> caminoRepartidor) {
        this.caminoRepartidor = caminoRepartidor;
    }

    @Override
    public String toString() {
        return "repartidor" + " id: " + identificador + " camino establecido " + caminoRepartidor;
    }

    public void init() {
        init(caminoRepartidor.getVerticeInicio());
    }

    public void init(GVertex<V> pathStart) {

        new Thread() {
            @Override
            public void run() {
                GVertex<V> v0 = pathStart;
                List<GVertex<V>> vs = caminoRepartidor.getRuta();

                Iterator<GVertex<V>> i = vs.getIterator();
                while (i.hasNext()) {

                    p0 = v0.getPosition();

                    // Se define el criterio para seleccionar
                    // el siguiente vértice.
                    GVertex<V> v1 = i.getNext();
                    p1 = v1.getPosition();

//                    System.out.printf("v(%s): %s%n", v0.getInfo(), p0);
//                    System.out.printf("v(%s): %s%n", v1.getInfo(), p1);
                    t = 0.0;
                    while (t <= 1.0) {
                        t += DT;
                        try {
                            Thread.sleep(MAX_WAIT);
                        } catch (InterruptedException ex) {
                        }
                    }
                    v0 = v1;
                }
            }

        }.start();

    }

    public void paint(Graphics bg, Rectangle bounds) {

        Graphics2D g = (Graphics2D) bg;

//                Iterator<Edge<V, E>> i = this.grafo.getEdges().getIterator();
//        while (i.hasNext()) {
//            Edge<V, E> e = i.getNext();
//
//        /*dibuja el trazo que une cada vertice*/
//            g.setStroke(TRAZO_BASE);
//            g.setColor(Color.WHITE);
//            g.drawLine(
//                    (int) e.getTail().getPosition().x,
//                    (int) e.getTail().getPosition().y,
//                    (int) e.getHead().getPosition().x,
//                    (int) e.getHead().getPosition().y
//            );
//
//        /*Dibuja una linea al centro del trazo que une cada vertice*/
//            g.setStroke(new BasicStroke(1f));
//            g.setColor(Color.BLACK);
//            g.drawLine(
//                    (int) e.getTail().getPosition().x,
//                    (int) e.getTail().getPosition().y,
//                    (int) e.getHead().getPosition().x,
//                    (int) e.getHead().getPosition().y
//            );
//        }
        if (p0 != null) {
            g.setStroke(TRAZO_MARCADOR);
            g.setColor(Color.RED);

            Image bkgnd = null;
            try {
                bkgnd = ImageIO.read(getClass().getResourceAsStream(ubicacionParcialImagen));
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }

            g.drawString(this.identificador, (int) ((p0.x + t * (p1.x - p0.x)) - S1 / 2),
                    (int) ((p0.y + t * (p1.y - p0.y)) - S1 / 2));
            g.drawImage(bkgnd, (int) ((p0.x + t * (p1.x - p0.x)) - S1 / 2),
                    (int) ((p0.y + t * (p1.y - p0.y)) - S1 / 2), null);

        }
    }

    public void update(Observable obs, Object evt) {
        throw new UnsupportedOperationException();
    }

    private static final Stroke TRAZO_MARCADOR = new BasicStroke(8f);
    private Point2D.Float p0;
    private Point2D.Float p1;
    private double t = 0.0;
    private static final int S1 = 56;
    private static final int MAX_WAIT = 25;
    private static final double DT = 0.035;
}
