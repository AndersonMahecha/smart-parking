async function readTagId(port) {
  const encoder = new TextEncoder();
  const decoder = new TextDecoder();
  const reader = port.readable.getReader();
  const writer = port.writable.getWriter();
  await writer.write(encoder.encode("*"));
  writer.releaseLock();
  let serial = "";
  try {
    let done = true;
    while (done) {
      const { value } = await reader.read();

      serial += decoder.decode(value);
      if (serial.length >= 8) {
        return String(serial);
      }
    }
  } catch (error) {
    // Handle |error|...
  } finally {
    reader.releaseLock();
    writer.releaseLock();
  }
  this.reading = false;
}

export { readTagId };
