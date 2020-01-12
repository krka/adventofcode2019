#include division.asm

int N = input()
int i = 0
int sum = 0

while i < N
  int n = input()
  sum = sum + div(n, 3) - 2
  i = i + 1
end
output(sum)
