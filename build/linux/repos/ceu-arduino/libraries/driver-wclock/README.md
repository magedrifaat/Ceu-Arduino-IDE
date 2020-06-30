# Wall-Clock Functionality

Control the passage of time from the real world measured in *seconds*,
*microseconds*, etc.

## API

### Includes

```
#include "wclock.ceu"
```

### Input

#### Time units

Awaits for units of time: `s`, `ms`, `us`.

See also:
    - https://ceu-lang.github.io/ceu/out/manual/v0.30/statements/#timer

*NOTE: The maximum amount of time is around 4 seconds.*

### Code Abstractions

#### WCLOCK_Now

Gets the number of elapsed microseconds since the board reset.

```
code/call WCLOCK_Now (none) -> u32;
```

Parameters:

- `none`

Return:

- `u32`: number of microseconds

*NOTE: Only works if a time unit await is active.*

*NOTE: The number overflows around every 4 seconds.*

### WCLOCK_Freeze

Freezes the whole program for a given number of microseconds.

```
code/call WCLOCK_Freeze (var u32 us) -> none do
```

Parameters:

- `u32`: number of milliseconds

Return:

- `none`

## Examples

### Periodic Switching

Blinks an LED connected to pin 13:

```
#include "out.ceu"
#include "wclock.ceu"

output high/low OUT_13;

loop do
    emit OUT_13(on);
    await 1s;
    emit OUT_13(off);
    await 1s;
end
```

### Print Current Time

Print current time on every second:

```
#include "wclock.ceu"

{ Serial.begin(9600); }

loop do
    await 1s;
    var u32 us = call WCLOCK_NOW();
    {
        Serial.print("MS ");
        Serial.println(@us/1000);
    }
    emit WCLOCK_Freeze(10000);
end
```
