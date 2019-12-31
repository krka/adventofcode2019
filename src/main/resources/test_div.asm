#include division.asm

int N = 0
int D = 0
int Q = 0
int R = 0

label loop
input N
input D
call div N D : Q R
output Q
output R
jump loop
