import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class AskPeer1 {

    private static JFrame frame;
    private static JTextArea textArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });

        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            appendText("Peer1 is waiting for connection...");

            Socket clientSocket = serverSocket.accept();
            appendText("Peer1 is connected to Peer2.");

            communicateWithPeer(clientSocket);

            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Peer1 GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void communicateWithPeer(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

        String messageFromPeer2 = reader.readLine();
        appendText("Message received from Peer2: " + messageFromPeer2);

        if (messageFromPeer2.equals("REQUEST_FILE")) {
            // Allow the user to choose the file interactively
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                appendText("Selected file to send: " + selectedFile.getName());
                sendFile(clientSocket, selectedFile);
            } else {
                appendText("File selection canceled.");
            }
        }
    }

    private static void sendFile(Socket clientSocket, File fileToSend) {
        try {
            appendText("Sending file: " + fileToSend.getName() + " (Size: " + fileToSend.length() + " bytes)");

            try (OutputStream outputStream = clientSocket.getOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                 FileInputStream fileInputStream = new FileInputStream(fileToSend)) {

                objectOutputStream.writeObject(new FileDetails(fileToSend.getName(), fileToSend.length()));

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                appendText("File sent successfully.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendText(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
        });
    }

    static class FileDetails implements Serializable {
        private final String name;
        private final long size;

        public FileDetails(String name, long size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public long getSize() {
            return size;
        }
    }
}
