# aero
Distributed stream processing framework

## aero-worker
- receive jobs via websocket and respond, initially JSON
- update websocket to support binary format
- implement health status page
- consul registrator
    - on startup registers itself against consul
    - consul to monitor health and provide service discovery
    - periodically update worker flow state in consul
- case class serialisation/deserailisation - include compression
- support flow submission
    - needs some design
- job processor for processing job queue
- job sender - resolves next processor and sends job to it (Simple round robin first)
