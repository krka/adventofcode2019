int i = 0
int k = 1
int kd = 0
int kdneg = 0
int q = 0
int neg_n = 0
int neg_d = 0
array[64] powtwo

setup_powtwo:
powtwo[i] = k
k = k * 2
i = i + 1
if i != 64 jump setup_powtwo

func div(N, D)
  if not D jump nan
  neg_n = 1
  neg_d = 1
  if N >= 0 jump skip_neg_n
  neg_n = -1
  N = N * -1
skip_neg_n:
  if D >= 0 jump skip_neg_d
  neg_d = -1
  D = D * -1
skip_neg_d:
  i = 0
  kd = D
find_range:
  i = i + 1
  kd = kd * 2
  if N >= kd jump find_range

  q = 0
loop_start:
  i = i + -1
  k = powtwo[i]
  kd = k * D
  if N < kd jump next_iter
  q = q + k
  kdneg = kd * -1
  N = N + kdneg
next_iter:
  if i jump loop_start
  q = q * neg_d
  q = q * neg_n
  N = N * neg_n
  return q, N
nan:
  halt
endfunc
