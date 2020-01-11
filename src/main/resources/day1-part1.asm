#include division.asm

int N = input()
int i = 0
int sum = 0

loop:
  int n = input()
  sum = sum + div(n, 3) - 2
  i = i + 1
if i < N jump loop
output(sum)
