int i = 0
int k = 1
int kd = 0
int q = 0
int neg_n = 0
int neg_d = 0
int extra = 0
array[64] powtwo

setup_powtwo:
powtwo[i] = k
k = k * 2
i = i + 1
if i < 64 jump setup_powtwo

func div(N, D)
  q = 0
  if ! D jump nan
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
  k = 1
find_range:
  i = i + 1
  k = k * 2
  kd = kd * 2
  if N >= kd jump find_range

  extra = i - 64
  if extra > 0 jump setup_extra
  jump loop_start

setup_extra:
  array[extra] powtwo_extra
  i = 0
  k = powtwo[63]
setup_extra_loop:
  k = k * 2
  powtwo_extra[i] = k
  i = i + 1
  if i < extra jump setup_extra_loop

extra_loop_start:
  i = i - 1
  k = powtwo_extra[i]
  kd = k * D
  if N < kd jump extra_next_iter
  q = q + k
  N = N - kd
extra_next_iter:
  if i jump extra_loop_start
i = 64

loop_start:
  i = i - 1
  k = powtwo[i]
  kd = k * D
  if N < kd jump next_iter
  q = q + k
  N = N - kd
next_iter:
  if i jump loop_start
  q = q * neg_d * neg_n
  N = N * neg_n
  return q, N
nan:
  throw()
endfunc
