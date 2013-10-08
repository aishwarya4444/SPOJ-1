import java.io.*;
import java.util.*;

public class lineup
{
    private static Reader in;
    private static PrintWriter out;

    public static void main (String [] args) throws IOException {
        in = new Reader ();
        out = new PrintWriter (System.out, true);
        
        int t = in.nextInt ();
        while (t-- > 0) {
            cap = new int [24][24]; cost = new int [24][24];
            for (int i = 0; i < 11; i++) 
                for (int j = 0; j < 11; j++) {
                    int p = in.nextInt ();
                    if (p == 0) continue;
                    addEdge (i, j + 11, 1, 100 - p);
                }
            for (int i = 0; i < 11; i++) addEdge (22, i, 1, 0);
            for (int j = 0; j < 11; j++) addEdge (j + 11, 23, 1, 0);
            int [] ans = flow (22, 23);
            out.println (100 * ans [0] - ans [1]);
        }
    }
    
    private static int N = 24;
    public static final int INF = 1 << 25;
    private static int [][] cap, cost;
    private static void addEdge (int x, int y, int w, int c) {
        cap [x][y] = w;
        cost [x][y] = c;
        cost [y][x] = -c;
    }
    
    private static int [] pot;
    private static int [] flow (int source, int sink) {
        int ans_flow = 0, ans_cost = 0;
        pot = new int [N];
        
        while (true) {        
            boolean [] used = new boolean [N];
            int [] dist = new int [N], prev = new int [N];
            Arrays.fill (dist, INF);
            dist [source] = 0;
            
            while (true) {
                int x = -1;
                for (int i = 0; i < N; i++)
                    if (dist [i] != INF && !used [i] 
                        && (x == -1 || dist [i] < dist [x]))
                            x = i;
                            
                if (x == -1) break;
                
                used [x] = true;
                for (int i = 0; i < N; i++)
                    if (cap [x][i] > 0 
                        && dist [x] + cost [x][i] + pot [x] - pot [i] < dist [i]) {
                            dist [i] = dist [x] + cost [x][i] + pot [x] - pot [i];
                            prev [i] = x;
                    }
            }
            
            if (!used [sink]) break;
            
            int ansf = INF, ansc = 0;
            for (int x = sink; x != source; x = prev [x]) 
                ansf = Math.min (ansf, cap [prev [x]][x]);
             
            ans_flow += ansf;
            for (int x = sink; x != source; x = prev [x]) {
                ansc += cost [prev [x]][x] * ansf;
                cap [prev [x]][x] -= ansf;
                cap [x][prev [x]] += ansf;
            }
            
            for (int i = 0; i < N; i++)
                pot [i] += dist [i];
                
            ans_cost += ansc;
        }
        
        return new int [] {ans_flow, ans_cost};
    }
}

/** Faster input **/
class Reader {
    final private int BUFFER_SIZE = 1 << 16;private DataInputStream din;private byte[] buffer;private int bufferPointer, bytesRead;
    public Reader(){din=new DataInputStream(System.in);buffer=new byte[BUFFER_SIZE];bufferPointer=bytesRead=0;}
    public Reader(String file_name)throws IOException{din=new DataInputStream(new FileInputStream(file_name));buffer=new byte[BUFFER_SIZE];bufferPointer=bytesRead=0;}
    public String readLine()throws IOException{byte[] buf=new byte[1024];int cnt=0,c;
        while((c=read())!=-1){if(c=='\n')break;buf[cnt++]=(byte)c;}return new String(buf,0,cnt);}
    public int nextInt()throws IOException{int ret=0;byte c=read();while(c<=' ')c=read();boolean neg=(c=='-');
        if(neg)c=read();do{ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');if(neg)return -ret;return ret;} 
    public long nextLong()throws IOException{long ret=0;byte c=read();while(c<=' ')c=read();boolean neg=(c=='-');
        if(neg)c=read();do{ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');if(neg)return -ret;return ret;}
    public double nextDouble()throws IOException{double ret=0,div=1;byte c=read();while(c<=' ')c=read();boolean neg=(c=='-');if(neg)c = read();do {ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');
        if(c=='.')while((c=read())>='0'&&c<='9')ret+=(c-'0')/(div*=10);if(neg)return -ret;return ret;}
    private void fillBuffer()throws IOException{bytesRead=din.read(buffer,bufferPointer=0,BUFFER_SIZE);if(bytesRead==-1)buffer[0]=-1;}
    private byte read()throws IOException{if(bufferPointer==bytesRead)fillBuffer();return buffer[bufferPointer++];}
    public void close()throws IOException{if(din==null) return;din.close();}
}