#!/usr/bin/env python
# Licensed to Cloudera, Inc. under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  Cloudera, Inc. licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""
Configuration options for the reverse monitor application.
"""
from django.utils.translation import ugettext_lazy as _

from desktop.lib.conf import Config, ConfigSection

DATA_SERVICE = ConfigSection(
  key='di-service',
  help=_('Configuration options for Oauth 1.0 authentication'),
  members=dict(
    DI_DATA_SERVICE_URL = Config(
      key="di_data_service_url",
      help=_("The Consumer key of the application."),
      type=str,
      default="http://localhost:8080/di-data-service/"
    )
  )
)
