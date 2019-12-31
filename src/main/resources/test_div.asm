#include division.asm

int N = 0
int D = 0
int Q = 0
int R = 0

loop:
N = input()
D = input()
Q, R = div(N, D)
output(Q)
output(R)
jump loop
