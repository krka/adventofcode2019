#include division.asm

int N = 0
int D = 0
int Q = 0
int R = 0

while 1 do
  N = input()
  D = input()
  Q, R = div(N, D)
  output(Q)
  output(R)
end
