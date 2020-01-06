int D2 = 0
int k2 = 0
int q = 0
int r = 0

func divInner(N, D, k)
  if N < D jump ret0

  D2 = 2 * D
  k2 = 2 * k

  q, r = divInner(N, D2, k2)
  if D > r jump skip_incr
  q = q + k
  r = r - D
skip_incr:
  return q, r
ret0:
  return 0, N
endfunc

func div(N, D)
  if not D jump NaN
  int neg_n = 1
  int neg_d = 1
  if N >= 0 jump skip_neg_n
  neg_n = -1
  N = N * -1
skip_neg_n:
  if D >= 0 jump skip_neg_d
  neg_d = -1
  D = D * -1
skip_neg_d:
  D, N = divInner(N, D, 1)
  D = D * neg_d
  D = D * neg_n
  N = N * neg_n
  return D, N
NaN:
  halt
endfunc
