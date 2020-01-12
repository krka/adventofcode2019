int i = 0
int k = 1
int kd = 0
int q = 0
int neg_n = 0
int neg_d = 0
int extra = 0
array[64] powtwo

while i < 64
  powtwo[i] = k
  k = k * 2
  i = i + 1
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

  i = 0
  kd = D
  k = 1
  while N >= kd
    i = i + 1
    k = k * 2
    kd = kd * 2
  end

  extra = i - 64
  if extra > 0 then
    array[extra] powtwo_extra
    i = 0
    k = powtwo[63]

    while i < extra
      k = k * 2
      powtwo_extra[i] = k
      i = i + 1
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
