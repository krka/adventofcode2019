int D2 = 0
int k2 = 0
int q = 0
int r = 0

func divInner(N, D, k)
  if N < D then
   return 0, N
  endif

  D2 = 2 * D
  k2 = 2 * k

  q, r = divInner(N, D2, k2)
  if D <= r then
    q = q + k
    r = r - D
  endif
  return q, r
endfunc

func div(N, D)
  if ! D then
    throw()
  endif
  int neg_n = 1
  int neg_d = 1
  if N < 0 then
    neg_n = -1
    N = -N
  endif
  if D < 0 then
    neg_d = -1
    D = -D
  endif
  D, N = divInner(N, D, 1)
  return D * neg_d * neg_n, N * neg_n
endfunc
