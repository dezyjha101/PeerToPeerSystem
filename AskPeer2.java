import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class AskPeer2 {

    private static JFrame frame;
    private static JTextArea textArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });

        try {
            // Replace "localhost" with the actual IP address or hostname of the machine running AskPeer1
            Socket clientSocket = new Socket("localhost", 8081); // Replace with the correct IP address or hostname
            appendText("Peer2 is connected to Peer1.");

            communicateWithPeer(clientSocket);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Peer2 GUI");
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

        writer.println("REQUEST_FILE");

        ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        try {
            AskPeer1.FileDetails fileDetails = (AskPeer1.FileDetails) objectInputStream.readObject();
            appendText("Receiving file: " + fileDetails.getName() + " (Size: " + fileDetails.getSize() + " bytes)");

            receiveFile(clientSocket, fileDetails);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(Socket clientSocket, AskPeer1.FileDetails fileDetails) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileDetails.getName())) {
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            appendText("File received successfully.");
        }
    }

    private static void appendText(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
        });
    }
}
