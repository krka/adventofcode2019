func assert(condition)
  if ! condition then
    throw()
  end
end

func strlen(s)
  int i = 0
  while 1 do
    if s[i] == 0 then
       return i
    end
    i = i + 1
  end
  # unreachable
  throw()
end

func strcmp(s1, s2)
  for i = 0, 1 do
    int c1 = s1[i]
    int c2 = s2[i]
    int diff = c1 - c2
    if diff || c1 == 0 || c2 == 0 then
      return diff
    end
  end
  # unreachable
  throw()
end

func memcpy(n, src, dest)
  while n > 0 do
    n = n - 1
    dest[n] = src[n]
  end
end
