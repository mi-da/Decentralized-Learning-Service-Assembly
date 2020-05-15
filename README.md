# Decentralized Learning for Self-Adaptive QoS-Aware and Energy-Aware Service Assembly
This page contains the simulation code to replicate the results of the paper: Emergent Assembly

### Instructions
1. Download the Java Project "Decentralized-Learning-Service-Assembly" and import it in your IDE as a Java project
2. Link the provided libraries in "ext-lib" to the project
3. Input the program argument "configs/mida-assembly-config.txt" (i.e., the configuration file of PeerSim)
4. The main class to run the experiments is "peersim.Simulator"

### Configuration Parameters
The file configs/mida-assembly-config.txt contains the configurations parameter for the simulation. The main parameters are:
- NETWORK_SIZE: The number of services to assemble (note that for the provided local quality functions the system starts saturating aroung 1000 nodes)
- TYPES: Number of types of services
- M: learning window of the proposed algorithm
- STRATEGY: The algorithm that the simulation run. We implemented 4 strategies: 1) emergent, 2) random, 3) greedy, 4) shaerf. The first strategy is the one proposed in this work.

### Contact 
If you have additional questions do not hesitate to contact me: https://lnu.se/en/staff/mirko.dangelo/
