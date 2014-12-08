PH2-NLP
=======

PA2 - NLP coursera Columbia University.
Files include:
1. BaseLine.java - run part2 , call ckyAlg and return file with parse trees obtained from test data.
2. Emission - hold Maps for all rules and probabilities of binary,unary and noneterminal.
3. Utils - the project core - read and write files , handling with JSONArray
4. ResultFormat.java - hold all subtree obtained during ckyAlg calculation and save the probability for each subtree.

Assignments parts:
1. read train data and return new counts after replacing each infrequent with "_RARE_" symbol.
2. calculate probabilities for each rule in train data
3. run ckyAlg and return file with predicted parse trees.
