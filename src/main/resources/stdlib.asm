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

int peek = -1

func skip_non_int()
  if peek != -1 then
    if peek >= '0' && peek <= '9' then
      return
    end
  end

  while 1 do
    int x = input()
    if x >= '0' && x <= '9' then
      peek = x
      return
    end
  end
  return
end

func skip_non_chars()
  if peek != -1 then
    if peek >= 'a' && peek <= 'z' then
      return
    end
  end

  while 1 do
    int x = input()
    if x >= 'a' && x <= 'z' then
      peek = x
      return
    end
  end
  return
end

func read_int()
  int res = 0

  if peek != -1 then
    if peek < '0' || peek > '9' then
      return -1
    else
      res = res * 10 + (peek - '0')
      peek = -1
    end
  end

  while 1 do
    int x = input()
    if x < '0' || x > '9' then
      peek = x
      return res
    else
      res = res * 10 + (x - '0')
    end
  end
  return -1
end

func read_chars(buf)
  int i = 0

  if peek != -1 then
    if peek < 'a' || peek > 'z' then
      buf[i] = 0
      return
    else
      buf[i] = peek
      i = i + 1
      peek = -1
    end
  end

  while 1 do
    int x = input()
    if x < 'a' || x > 'z' then
      peek = x
      buf[i] = 0
      return
    else
      buf[i] = x
      i = i + 1
    end
  end
  buf[i] = 0
  return
end
