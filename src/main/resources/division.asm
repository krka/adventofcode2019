int k = 1
int kd = 0
int q = 0
int neg_n = 0
int neg_d = 0
int extra = 0
array[64] powtwo

for i = 0, i < 64 do
  powtwo[i] = k
  k = k * 2
end

func div(N, D)
  q = 0
  if ! D then
    throw()
  end

  neg_n = 1
  neg_d = 1
  if N < 0 then
    neg_n = -1
    N = -N
  end

  if D < 0 then
    neg_d = -1
    D = -D
  end

  int i = 0
  kd = D
  k = 1
  for i = 0, N >= kd do
    k = k * 2
    kd = kd * 2
  end

  extra = i - 64
  if extra > 0 then
    array[extra] powtwo_extra
    k = powtwo[63]

    for i = 0, i < extra do
      k = k * 2
      powtwo_extra[i] = k
    end

    while i > 0
      i = i - 1
      k = powtwo_extra[i]
      kd = k * D
      if N >= kd then
        q = q + k
        N = N - kd
      end
    end
    i = 64
  end

  while i > 0
    i = i - 1
    k = powtwo[i]
    kd = k * D
    if N >= kd then
      q = q + k
      N = N - kd
    end
  end
  return q * neg_d * neg_n, N * neg_n
end
