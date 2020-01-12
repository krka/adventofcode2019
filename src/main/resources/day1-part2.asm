#include division.asm

func rec(n, acc)
  int n2 = div(n, 3) - 2
  if n2 <= 0 then
    return acc
  endif
  int res = rec(n2, acc + n2)
  return res
endfunc

int N = input()
int i = 0
int sum = 0

loop:
  int n = input()
  sum = sum + rec(n, 0)
  i = i + 1
if i < N jump loop
output(sum)
