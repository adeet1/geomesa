/***********************************************************************
 * Copyright (c) 2013-2017 Commonwealth Computer Research, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php.
 ***********************************************************************/

package org.locationtech.geomesa.tools.status

import org.locationtech.geomesa.index.geotools.GeoMesaDataStore
import org.locationtech.geomesa.tools._
import org.locationtech.geomesa.utils.conf.GeoMesaProperties._

import scala.util.control.NonFatal

trait VersionRemoteCommand[DS <: GeoMesaDataStore[_, _, _]] extends DataStoreCommand[DS] {

  override val name: String = "version-remote"

  override def execute(): Unit = {
    Command.output.info(s"Local GeoMesa tools version: $ProjectVersion")
    Command.output.info(s"Local Commit ID: $GitCommit")
    Command.output.info(s"Local Branch: $GitBranch")
    Command.output.info(s"Local Build date: $BuildDate")
    try {
      val iterVersions = withDataStore(_.getVersion._2)
      Command.output.info(s"Distributed runtime version${ if (iterVersions.size > 1) "s" else "" }: " +
          iterVersions.mkString(", "))
      if (iterVersions.size > 1) {
        Command.output.warn("WARNING: multiple iterator versions detected, check your cluster installation")
      }
    } catch {
      case NonFatal(e) => Command.user.error("Could not get distributed version:", e)
    }
  }
}
