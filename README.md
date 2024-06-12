<h1>Peer-to-Peer File Sharing System</h1>
<p>This project is a simple peer-to-peer (P2P) file sharing system implemented in Java. The system allows two peers to connect and share files between them. The current implementation includes two main components: AskPeer1 and AskPeer2.</p>

<h3>Features</h3>
<ul>
<li>Graphical User Interface (GUI): Both peers have a GUI implemented using Java Swing.</li>
<li>Server Socket: AskPeer1 listens on a specified port (8081) for incoming connections.</li>
<li>File Selection: Peer1 can select a file to send using a file chooser dialog.</li>
<li>File Transfer: The selected file is sent from Peer1 to Peer2.</li>
<li>Message Communication: Basic message communication between peers.</li>
</ul>  
<h3>Prerequisites</h3>
<li>Java Development Kit (JDK) 8 or higher installed on both machines.</li>
<h3>Usage</h3>
<ol>
<li>Start AskPeer1 on one machine. The GUI will display a message indicating it is waiting for a connection.</li>
<li>Start AskPeer2 on another machine (or the same machine if testing locally). The GUI will display a message indicating it has connected to Peer1.</li>
<li>Peer2 will automatically send a "REQUEST_FILE" message to Peer1.</li>
<li>Peer1's user will be prompted to select a file to send to Peer2.</li>
<li>Once the file is selected, it will be transferred to Peer2, and both GUIs will update to show the status of the transfer.</li>
</ol>
