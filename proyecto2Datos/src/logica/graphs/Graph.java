package logica.graphs;

import java.awt.geom.Point2D;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lists.Iterator;
import lists.List;
import lists.SimpleLinkedList;

/**
 *
 * @author Bryan
 * @param <V>
 * @param <E>
 */

@XmlRootElement(name="Graph")
@XmlType(propOrder = {"vertex","edges"})
public class Graph<V, E> {

    public Graph() {
        this(new SimpleLinkedList<>(), new SimpleLinkedList<>());
    }

    public Graph(List<GVertex<V>> vertices, List<Edge<V, E>> edges) {
        this.vertex = vertices;
        this.edges = edges;
    }

    public GVertex<V> getVertex(V v) {
        GVertex<V> r = null;
        Iterator<GVertex<V>> i = vertex.getIterator();
        while (i.hasNext()) {
            GVertex<V> ptr = i.getNext();
            if (ptr.getInfo().equals(v)) {
                r = ptr;
                break;
            }
        }
        return r;
    }

    public List<GVertex<V>> getAdjacent(GVertex<V> v) {
        List<GVertex<V>> r = new SimpleLinkedList<>();
        Iterator<Edge<V, E>> i = edges.getIterator();
        while (i.hasNext()) {
            Edge<V, E> e = i.getNext();
            if (e.getHead().getInfo().equals(v.getInfo())) {
                r.addLast(e.getTail());
            }
            if (e.getTail().getInfo().equals(v.getInfo())) {
                r.addLast(e.getHead());
            }
        }
        return r;
    }

    public void add(V v, Point2D.Float position) {
        vertex.addLast(new GVertex<>(v, position));
    }

    public void add(V v) {
        vertex.addLast(new GVertex<>(v, new Point2D.Float(DX + df.x, DY + df.y)));

        if (px < MX) {
            df.x += DX;
            px++;
        } else {
            df.x = 0;
            df.y += DY;
            px = 0;
        }
    }

    public void add(GVertex<V> tail, GVertex<V> head, E w) {
        if ((tail == null) || (head == null)) {
            throw new NullPointerException("No existe el vértice.");
        }
        edges.addLast(new Edge<>(tail, head, w));
    }

    public void add(V t, V h, E w) {
        add(Graph.this.getVertex(t), Graph.this.getVertex(h), w);
    }

    public void add(V t, V h) {
        add(t, h, null);
    }

    @Override
    public String toString() {
        return String.format("G: (%n   V: %s,%n   E: %s%n)",
                vertex, edges);
    }

//    public Rectangle getBounds() {
//        float x0, x1, y0, y1;
//        x0 = x1 = y0 = y1 = 0f;
//        boolean f = false;
//
//        Iterator<GVertex<V>> i = vertex.getIterator();
//        while (i.hasNext()) {
//            GVertex<V> v = i.getNext();
//
//            if (!f) {
//                x0 = x1 = v.getPosition().x;
//                y0 = y1 = v.getPosition().y;
//            }
//            f = true;
//
//            x0 = Math.min(x0, v.getPosition().x);
//            x1 = Math.max(x1, v.getPosition().x);
//            y0 = Math.min(y0, v.getPosition().y);
//            y1 = Math.max(y1, v.getPosition().y);
//        }
//
//        if (!f) {
//            throw new IllegalArgumentException();
//        }
//
//        Rectangle r = new Rectangle(
//                (int) (x0), (int) (y0),
//                (int) (x1 - x0), (int) (y1 - y0)
//        );
//        r.grow(diamentroVertice / 2, diamentroVertice / 2);
//        return r;
//    }
//    public void paint(Graphics bg, Rectangle bounds) {
//        Graphics2D g = (Graphics2D) bg;
//
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//        g.setColor(Color.DARK_GRAY);
//        g.setStroke(TRAZO_GUIA);
//        Rectangle b = getBounds();
////        g.drawRect(b.x, b.y, b.width, b.height);
//
//        g.setFont(TIPO_BASE);
//        FontMetrics fm = g.getFontMetrics();
//
////        Iterator<Edge<V, E>> i = edges.getIterator();
////        while (i.hasNext()) {
////            Edge<V, E> e = i.getNext();
//
//            /*dibuja el trazo que une cada vertice*/
////            g.setStroke(TRAZO_BASE);
////            g.setColor(Color.WHITE);
////            g.drawLine(
////                    (int) e.getTail().getPosition().x,
////                    (int) e.getTail().getPosition().y,
////                    (int) e.getHead().getPosition().x,
////                    (int) e.getHead().getPosition().y
////            );
//
//            /*Dibuja una linea al centro del trazo que une cada vertice*/
////            g.setStroke(new BasicStroke(1f));
////            g.setColor(Color.BLACK);
////            g.drawLine(
////                    (int) e.getTail().getPosition().x,
////                    (int) e.getTail().getPosition().y,
////                    (int) e.getHead().getPosition().x,
////                    (int) e.getHead().getPosition().y
////            );
////        }
//
////        g.setStroke(TRAZO_VERTICE);
////        Iterator<GVertex<V>> j = vertex.getIterator();
////        while (j.hasNext()) {
////            GVertex<V> v = j.getNext();
//
////            g.setColor(Color.GRAY);
////            g.fillOval((int) v.getPosition().x - diamentroVertice / 2 + 4,
////                    (int) v.getPosition().y - diamentroVertice / 2 + 4,
////                    diamentroVertice, diamentroVertice);
////            g.setColor(Color.WHITE);
////            g.fillOval((int) v.getPosition().x - diamentroVertice / 2,
////                    (int) v.getPosition().y - diamentroVertice / 2,
////                    diamentroVertice, diamentroVertice);
////            g.setColor(Color.BLACK);
////            g.drawOval((int) v.getPosition().x - S0 / 2,
////                    (int) v.getPosition().y - S0 / 2,
////                    S0, S0);
//
////            String tt = String.format("%s", v.getInfo());
////            g.setColor(Color.GRAY);
////            g.drawString(tt,
////                    v.getPosition().x - fm.stringWidth(tt) / 2,
////                    v.getPosition().y + fm.getAscent() / 2);
////        }
//
//        if (p0 != null) {
//            g.setStroke(TRAZO_MARCADOR);
//            g.setColor(Color.RED);
//            
//           Image bkgnd=null;
//            try {
//                 bkgnd = ImageIO.read(getClass().getResourceAsStream("imaRepartidor/repartidor4.png"));
//            } catch (IOException ex) {
//                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
//            }
//     
//            g.drawString("111111",(int) ((p0.x + t * (p1.x - p0.x)) - S1 / 2),
//                    (int) ((p0.y + t * (p1.y - p0.y)) - S1 / 2));
//            g.drawImage(bkgnd, (int) ((p0.x + t * (p1.x - p0.x)) - S1 / 2),
//                    (int) ((p0.y + t * (p1.y - p0.y)) - S1 / 2), (ImageObserver) null);
////            g.drawOval(
////                    (int) ((p0.x + t * (p1.x - p0.x)) - S1 / 2),
////                    (int) ((p0.y + t * (p1.y - p0.y)) - S1 / 2),
////                    S1, S1);
//        }
//    }
//
//    public void update(Observable obs, Object evt) {
//        throw new UnsupportedOperationException();
//    }
//    public String getAdjacencyInfo() {
//        StringBuilder r = new StringBuilder();
//        Iterator<GVertex<V>> i = vertex.getIterator();
//        while (i.hasNext()) {
//            GVertex<V> v = i.getNext();
//            r.append(String.format("%s: ", v.getInfo()));
//            Iterator<GVertex<V>> j = getAdjacent(v).getIterator();
//            while (j.hasNext()) {
//                r.append(String.format("%s, ", j.getNext().getInfo()));
//            }
//            r.append("\n");
//        }
//        return r.toString();
//    }
    
    @XmlElement(name = "Vertex")
    public List<GVertex<V>> getVertex() {
        return vertex;
    }
    
    @XmlElement(name = "Edges")
    public List<Edge<V, E>> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge<V, E>> edges) {
        this.edges = edges;
    }

    public void setVertex(List<GVertex<V>> vertex) {
        this.vertex = vertex;
    }

//    private static final float[] DASHES = {4f, 4f};
//    private static final Stroke TRAZO_BASE
//            = new BasicStroke(12f,
//                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, null, 0f);
//    private static final Stroke TRAZO_VERTICE = new BasicStroke(2f);
//    private static final Stroke TRAZO_MARCADOR = new BasicStroke(8f);
//
//    private static final Stroke TRAZO_GUIA
//            = new BasicStroke(1.0f,
//                    BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
//                    0f, DASHES, 0f);
//    private static final Font TIPO_BASE
//            = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
//
//    private static final int diamentroVertice = 25;
//    private static final int S1 = 56;
//
    private static final int DX = 72;
    private static final int DY = 64;
    private static final int MX = 6;
    private int px = 0;
    private final Point2D.Float df = new Point2D.Float(0, 0);
    private List<GVertex<V>> vertex;
    private List<Edge<V, E>> edges;
//
//    private static final int MAX_WAIT = 25;
//    private boolean active = false;
//    private Point2D.Float p0;
//    private Point2D.Float p1;
//    private static final double DT = 0.035;
//    private double t = 0.0;
}
