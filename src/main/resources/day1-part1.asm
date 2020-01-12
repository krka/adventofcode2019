#include division.asm

int N = input()
int sum = 0

for i = 0, i < N do
  int n = input()
  sum = sum + div(n, 3) - 2
end
output(sum)
