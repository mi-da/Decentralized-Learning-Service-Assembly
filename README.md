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
- STRATEGY: The selection criteria that the nodes adopt. We implemented multiple strategies:
  1. emergent [1]
  2. random
  3. greedy
  4. shaerf
  5. individual_energy
  6. overall_energy
  7. fair_energy
  
### References 
 
[1] M. D’Angelo, M. Caporuscio, V. Grassi, and R. Mirandola, ‘Decentralized learning for self-adaptive QoS-aware service assembly’, Future generations computer systems, vol. 108, pp. 210–227, 2020. https://doi.org/10.1016/j.future.2020.02.027.


### Contact 
If you have additional questions do not hesitate to contact me: https://lnu.se/en/staff/mirko.dangelo/
