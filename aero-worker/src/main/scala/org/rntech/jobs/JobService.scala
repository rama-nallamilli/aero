package org.rntech.jobs

import java.util.UUID

case class JobAdded(urn: String)
case class JobOperationFailed(msg: String) extends RuntimeException(msg)

class JobService {
  def queueJob(job: String): Either[JobOperationFailed, JobAdded] = {
    Right(JobAdded(UUID.randomUUID().toString))    
  }
}
