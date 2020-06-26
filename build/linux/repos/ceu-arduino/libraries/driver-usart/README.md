# USART (Universal Synchronous and Asynchronous Receiver-Transmitter)

## API

### Macros

This optional macro must be defined before including the driver:

```
#define USART_RX_BUF_N <fill-with-buffer-size>
```

The default size is `32` bytes.

### Includes

```
#include "usart.ceu"
```

### Code Abstractions

#### USART_Init

Initializes the USART.

```
code/await USART_Init (var int bps) -> NEVER;
```

Parameters:

- `int`: transmission speed in `bps`

Return:

- `NEVER`: never returns

#### USART_Tx

Transmits a given buffer.

```
code/await USART_Tx (var&[] byte buf) -> none;
```

Parameters:

- `&[] byte`: reference to buffer to transmit

Return:

- `none`: as soon as the transmission terminates

The given buffer is copied to the driver buffer, which transmits the bytes in
the background.

#### USART_Rx

Receives incoming bytes to a given buffer.

```
code/await USART_Rx (var&[] byte buf, var usize? n) -> none;
```

Parameters:

- `&[] byte`: reference to buffer to receive
- `usize?`:   minimum number of bytes to receive (default: as soon as something is received)

Return:

- `none`: as soon as the requested number of bytes is received

## Examples

`TODO`
