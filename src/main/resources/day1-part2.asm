#include division.asm

func rec(n, acc)
  int n2 = div(n, 3) - 2
  if n2 <= 0 then
    return acc
  end
  int res = rec(n2, acc + n2)
  return res
end

int N = input()
int sum = 0

for i = 0, i < N do
  int n = input()
  sum = sum + rec(n, 0)
end
output(sum)
